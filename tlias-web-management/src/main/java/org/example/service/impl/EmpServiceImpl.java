package org.example.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.example.mapper.EmpExprMapper;
import org.example.mapper.EmpMapper;
import org.example.pojo.*;
import org.example.service.EmpService;
import org.example.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class EmpServiceImpl implements EmpService {
    private final EmpMapper empMapper;
    private final EmpExprMapper empExprMapper;
    @Autowired
    public EmpServiceImpl(EmpMapper empMapper, EmpExprMapper empExprMapper) {
        this.empMapper = empMapper;
        this.empExprMapper = empExprMapper;
    }
//    @Override
//    public PageResult<Emp> page(int page, int size) {
//        long total = empMapper.count();
//        List<Emp> list = empMapper.list((page - 1) * size, size);
//        return new PageResult<Emp>(total, list);
//    }
    @Override
    public PageResult<Emp> page(EmpQueryParam empQueryParam) {
        PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize());
        List<Emp> empList = empMapper.list(empQueryParam);
        Page<Emp> p = (Page<Emp>) empList;
        return new PageResult<Emp>(p.getTotal(),p.getResult());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Emp emp) {
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            empMapper.insert(emp);
            List<EmpExpr> empExprList = emp.getExprList();
            if (empExprList != null && !empExprList.isEmpty()) {
                for (EmpExpr empExpr : empExprList) {
                    empExpr.setEmpId(emp.getId());
                }
                empExprMapper.insertBatch(empExprList);
            }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<Integer> ids) {
        empMapper.deleteByIds(ids);
        empExprMapper.deleteByEmpIds(ids);
    }

    @Override
    public Emp get(Integer id) {
        return empMapper.getEmpById(id);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Emp emp) {
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);
        empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));
        List<EmpExpr> empExprList = emp.getExprList();
        if (empExprList != null && !empExprList.isEmpty()) {
            for (EmpExpr empExpr : empExprList) {
                empExpr.setEmpId(emp.getId());
            }
            empExprMapper.insertBatch(empExprList);
        }

    }

    @Override
    public List<Emp> list() {
        List<Emp> empList = empMapper.getEmpList();
        return empList;
    }

    @Override
    public LoginInfo login(Emp emp) {
        Emp emp1 = empMapper.selectByUsernameAndPassword(emp);
        if(emp1 != null){
            log.info("登录成功：{}",emp1);
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", emp1.getId());
            claims.put("username", emp1.getUsername());
            String token = JwtUtils.generateToken(claims);
            return new LoginInfo(emp1.getId(), emp1.getUsername(), emp1.getName(), token);
        }
        return null;
    }
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String sendVerifyCode(String phone) {

        String verifycode = String.valueOf(RandomUtils.nextInt(100000,999999));
        log.info("【模拟短信】验证码：{}", verifycode);
        stringRedisTemplate.opsForValue().set("verifycode:" + phone, verifycode,1, TimeUnit.MINUTES);
        log.info("验证码已发送至手机：{}", phone);

        return verifycode;
    }

    @Override
    public LoginInfo loginByPhone(Emp emp) {
        Emp emp1 = empMapper.selectByPhone(emp.getPhone());
        if(emp1 != null){
            String cached = stringRedisTemplate.opsForValue().get("verifycode:" + emp.getPhone());
            if (cached == null) {
                log.info("验证码已过期或不存在");
                return null;        // 或者抛业务异常，前端提示"验证码已过期，请重新获取"
            }
            if (emp.getVerifyCode() != null && emp.getVerifyCode().equals(cached)) {
                log.info("登录成功：{}", emp1);
                Map<String, Object> claims = new HashMap<>();
                claims.put("id", emp1.getId());
                claims.put("username", emp1.getUsername());
                String token = JwtUtils.generateToken(claims);
                stringRedisTemplate.delete("verifycode:" + emp.getPhone());
                return new LoginInfo(emp1.getId(), emp1.getUsername(), emp1.getName(), token);
            }else{
                log.info("验证码错误");
            }
        }else {
            log.info("用户不存在：{}", emp.getPhone());
        }
        return null;
    }


}
