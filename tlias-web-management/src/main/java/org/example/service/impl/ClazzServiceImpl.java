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
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public PageResult<Clazz> get_page_query(ClazzQueryParam clazzQueryParam) {
        PageResult<Clazz> pageResult = null;
        String key = "clazz:list:"
                + clazzQueryParam.getName() + ":"
                + clazzQueryParam.getBegin() + ":"
                + clazzQueryParam.getEnd() + ":"
                + clazzQueryParam.getPage() + ":"
                + clazzQueryParam.getPageSize();

        if (redisTemplate.hasKey(key)) {
            log.info("命中缓存{}", key);
            Object cache = redisTemplate.opsForValue().get(key);

            if("empty".equals(cache)){
                log.info("缓存为空{},刷新空值", key);
                redisTemplate.opsForValue().set(key, "empty", 10, TimeUnit.MINUTES);
                return new PageResult<Clazz>(0, new ArrayList<Clazz>());
            }else{
                pageResult = (PageResult<Clazz>) cache;
                return pageResult;
            }
        }else{
            log.info("未命中缓存，查询数据库{}", key);
            PageHelper.startPage(clazzQueryParam.getPage(), clazzQueryParam.getPageSize());
            List<Clazz> clazzList = clazzMapper.selectByQueryParams(clazzQueryParam);
            if (clazzList.isEmpty()) {
                redisTemplate.opsForValue().set(key, "empty", 5, TimeUnit.MINUTES);
                log.info("出现缓存穿透{}，增添缓存空值", key);
                return new PageResult<Clazz>(0, new ArrayList<Clazz>());
            }else{
                log.info("数据库命中{}，返回数据", key);
                Page<Clazz> p = (Page<Clazz>) clazzList;
                pageResult = new PageResult<Clazz>(p.getTotal(),p.getResult());
                redisTemplate.opsForValue().set(key, pageResult,2,TimeUnit.MINUTES);
                return pageResult;
            }
        }
    }

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
