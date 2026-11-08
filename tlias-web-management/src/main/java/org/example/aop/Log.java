package org.example.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.mapper.OperateLogMapper;
import org.example.pojo.OperateLog;
import org.example.utils.CurrentHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class Log {
    private final OperateLogMapper operateLogMapper;
    public Log(OperateLogMapper operateLogMapper) {
        this.operateLogMapper = operateLogMapper;
    }

    @Around("@annotation(org.example.anno.LogOperation)")//寻找带这样标签的方法执行环绕
    public Object logOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;

        OperateLog operateLog = new OperateLog();
        operateLog.setOperateEmpId(getCurrentEmpId());
        operateLog.setOperateTime(LocalDateTime.now());
        operateLog.setClassName(joinPoint.getTarget().getClass().getName());
        operateLog.setMethodName(joinPoint.getSignature().getName());
        operateLog.setMethodParams(Arrays.toString(joinPoint.getArgs()));
        // 找到这行：
        String returnValueStr = result == null ? "null" : result.toString();
// 改成这行（超过 2000 字就截断）：
        operateLog.setReturnValue(returnValueStr.length() > 2000 ? returnValueStr.substring(0, 2000) : returnValueStr);
        operateLog.setCostTime(costTime);

        log.info("Operating Log: {}", operateLog);
        operateLogMapper.insert(operateLog);

        return result;
    }

    private Integer getCurrentEmpId() {
        return CurrentHolder.getCurrentId();
    }
}