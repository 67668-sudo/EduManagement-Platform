//package org.example.aop;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.springframework.stereotype.Component;
//
//@Aspect    // 👈 告诉 Spring：我是切面
//@Component // 👈 告诉 Spring：把我交给容器管理（不写这个，Spring 不知道有这个类）
//@Slf4j
//public class MyAspect1test {
//
////    @Pointcut("execution(* org.example.service..*.*(..))")
////    public void pt(){}
////
////    @Before("pt()")
////    public void before(){
////        log.info("before");
////    }
////
////    @Around("pt()")
////    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
////        log.info("before");
////        Object result = joinPoint.proceed();
////        log.info("after");
////        return result;
////    }
////
////    @After("pt()")
////    public void after(){
////        log.info("after");
////    }
////
////    @AfterReturning("pt()")
////    public void afterReturning(){
////        log.info("afterReturning");
////    }
//
//    @Before("@annotation(org.example.anno.LogOperation)")
//    public void afterThrowing(JoinPoint joinPoint){
//
//
//        log.info("afterThrowing");
//    }
//}
