package org.example.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.exception.ExistStudentException;
import org.example.exception.NotExistEmpException;
import org.example.mapper.ClazzMapper;
import org.example.mapper.EmpMapper;
import org.example.mapper.StudentMapper;
import org.example.pojo.*;
import org.example.utils.CacheClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.service.ClazzService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class ClazzServiceImpl implements ClazzService {
    private ClazzMapper clazzMapper;
    private EmpMapper empMapper;
    private StudentMapper studentMapper;

    @Autowired
    public void setEmpMapper(EmpMapper empMapper, ClazzMapper clazzMapper, StudentMapper studentMapper) {
        this.empMapper = empMapper;
        this.clazzMapper = clazzMapper;
        this.studentMapper = studentMapper;
    }

    //    ==================================================================
//      【互斥锁版本】—— 留作对比，应对「缓存击穿」
//    ==================================================================
//
//      触发场景：热点 key 刚过期的那一瞬间，大量并发请求同时进来。
//      因为没有锁，所有请求都会各自去查数据库 —— 这就是缓存击穿。
//
//
//    @Override
//    public PageResult<Clazz> get_page_query(ClazzQueryParam clazzQueryParam) throws InterruptedException {
//        // ========== 缓存 key：所有影响结果的参数都要进 key ==========
//        String key = "clazz:list:" + clazzQueryParam.getName() + ":" + clazzQueryParam.getBegin()
//                + ":" + clazzQueryParam.getEnd() + ":" + clazzQueryParam.getPage()
//                + ":" + clazzQueryParam.getPageSize();
//
//        // ========== 互斥锁 key：每个缓存 key 一把锁，不同条件互不影响 ==========
//        String lockKey = "lock:clazz:" + key;
//
//        int retryCount = 0;
//        int maxRetries = 3;   // 最多重试 3 次；建议改成 10~20（500ms~1s），3 次只有 150ms 偏短
//
//        while (retryCount < maxRetries) {
//
//            // ---------- ① 先查缓存 ----------
//            // 绝大多数请求在这里就返回了，根本走不到加锁那一步
//            Object cache = redisTemplate.opsForValue().get(key);
//            if (cache != null) {
//                if ("empty".equals(cache)) {
//                    // 空值标记：数据库里确实没有，直接返回空，不再打库（防穿透）
//                    return new PageResult<>(0L, new ArrayList<>());
//                }
//                return (PageResult<Clazz>) cache;
//            }
//
//            // ---------- ② 缓存没命中，抢锁 ----------
//            // setIfAbsent = Redis 的 SETNX，同一时刻只有一个人能抢到//返回的是包装类可能出现null
//            // 第三个参数 10 秒 TTL：即使进程崩了，锁也会自动过期，不会永久死锁
//            Boolean isLock = redisTemplate.opsForValue()
//                    .setIfAbsent(lockKey, "locked", 10, TimeUnit.SECONDS);
//
//            if (Boolean.TRUE.equals(isLock)) {
//                // 抢到了，说明由我负责"查库 + 写缓存"这一件事
//                try {
//                    // ---------- ③ 双重检查 ----------
//                    // 抢锁这几毫秒里，前一个持有锁的人可能刚把缓存写好
//                    // 不再确认一次的话，就会重复查库，缓存白写
//                    cache = redisTemplate.opsForValue().get(key);
//                    if (cache != null) {
//                        if ("empty".equals(cache)) {
//                            return new PageResult<>(0L, new ArrayList<>());
//                        }
//                        return (PageResult<Clazz>) cache;
//                    }
//
//                    // ---------- ④ 查数据库并回填缓存 ----------
//                    log.info("抢锁成功，查询数据库：{}", key);
//                    PageHelper.startPage(clazzQueryParam.getPage(), clazzQueryParam.getPageSize());
//                    List<Clazz> clazzList = clazzMapper.selectByQueryParams(clazzQueryParam);
//
//                    Page<Clazz> p = (Page<Clazz>) clazzList;
//                    PageResult<Clazz> pageResult = new PageResult<>(p.getTotal(), p.getResult());
//                    redisTemplate.opsForValue().set(key, pageResult, 30, TimeUnit.MINUTES);
//                    return pageResult;
//
//                } finally {
//                    // ---------- ⑤ 一定要释放锁 ----------
//                    // 放在 finally 里：查库抛异常时也能释放，否则只能等 10 秒 TTL 过期
//                    redisTemplate.delete(lockKey);
//                }
//            }
//
//            // ---------- ⑥ 没抢到锁：等 50ms 再来一次 ----------
//            // 注意等的是"缓存被别人写好"，不是"锁被放开"
//            // 下一次循环开头会先查缓存，命中就直接返回
//            log.info("没抢到锁，等待 50ms 后重试，当前第 {} 次", retryCount + 1);
//            Thread.sleep(50);
//            retryCount++;
//        }
//
//        // ---------- ⑦ 兜底：宁可报错，也不放行打数据库 ----------
//        // 如果这里改成"降级查库"，互斥锁就失去意义了——
//        // 因为没抢到锁的请求会全部涌入数据库，等于没加锁
//        log.error("获取锁重试次数耗尽，系统繁忙：{}", key);
//        throw new RuntimeException("当前访问人数过多，请稍后再试！");
//    }
//


//    ==================================================================
//      【无互斥锁版本】—— 留作对比，会引发「缓存击穿」
//    ==================================================================
//
//      触发场景：热点 key 刚过期的那一瞬间，大量并发请求同时进来。
//      因为没有锁，所有请求都会各自去查数据库 —— 这就是缓存击穿。
//
//      启用方式：把上面 get_page_query 的 @Override 去掉并改名，
//                再把下面这段的注释去掉。
@Autowired
private CacheClient cacheClient;

    @Override
    public PageResult<Clazz> get_page_query(ClazzQueryParam clazzQueryParam) {
        String key = "clazz:list:" + clazzQueryParam.getName() + ":" + clazzQueryParam.getBegin()
                + ":" + clazzQueryParam.getEnd() + ":" + clazzQueryParam.getPage()
                + ":" + clazzQueryParam.getPageSize();

        // 调用万能工具类
        PageResult<Clazz> pageResult = cacheClient.queryWithPassThrough(
                key,
                PageResult.class,
                () -> { // 👈 把具体的查库逻辑用 Lambda 包起来
                    PageHelper.startPage(clazzQueryParam.getPage(), clazzQueryParam.getPageSize());
                    List<Clazz> clazzList = clazzMapper.selectByQueryParams(clazzQueryParam);
                    Page<Clazz> p = (Page<Clazz>) clazzList;
                    return new PageResult<>(p.getTotal(), p.getResult());
                }
        );

        // 如果是防穿透命中的 empty，返回空结果
        if (pageResult == null) {
            return new PageResult<>(0L, new ArrayList<>());
        }
        return pageResult;
    }


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Transactional
    @Override
    public void Delete_calzz(Integer id) {
        Student student = studentMapper.selectStudentById(id);
        if (student != null) {
            throw new ExistStudentException("当前班级下还有学生，不能直接删除！");
        }

        empMapper.update_lose_job(id);
        clazzMapper.deleteClazz(id);

        Set<String> keys = redisTemplate.keys("clazz:list:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    @Transactional
    @Override
    public void add_clazz(Clazz clazz) {
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        if(clazz.getMasterId() != null){
            Emp emp = empMapper.search_job_by_id(clazz.getMasterId());
            if (emp == null) {
                throw new NotExistEmpException("不存在此员工，请先添加员工后再次尝试");
            }
        }
        clazzMapper.add_clazz(clazz);
        Set<String> keys = redisTemplate.keys("clazz:list:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    @Override
    public Clazz get_clazz(Integer id) {
        return clazzMapper.get_clazz(id);
    }

    @Transactional
    @Override
    public void update_clazz(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.update_clazz(clazz);
        Set<String> keys = redisTemplate.keys("clazz:list:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    @Override
    public List<Clazz> list() {
        return clazzMapper.list();
    }

}
