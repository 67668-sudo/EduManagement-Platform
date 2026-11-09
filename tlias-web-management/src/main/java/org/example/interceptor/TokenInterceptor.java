package org.example.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.utils.CurrentHolder;
import org.example.utils.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
//        System.out.println("requestURI = " + requestURI + ", method = " + method);
        if((requestURI.equals("/clazzs") && method.equals("GET")) || (requestURI.equals("/emps/list") && method.equals("GET"))){
            log.info("游客模式观看 clazzs 放行");
            return true;
        }else if (token == null || token.isEmpty()) {
            log.warn("拦截请求：{} {}", request.getMethod(), request.getRequestURI());
            log.warn("Token is missing");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        try{
            Claims claims = JwtUtils.parseToken(token);
            Integer empId = Integer.valueOf(claims.get("id").toString());
            // 1. 进门：存进储物柜
            CurrentHolder.setCurrentId(empId);
            log.info("Token is valid, 当前操作人ID: {}", empId);
        }catch(Exception e){
            log.warn("Token is invalid");
            log.warn("拦截请求：{} {}", request.getMethod(), request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }

    // 👈 2. 出门：请求彻底结束，清理储物柜，防止内存泄漏和串号！
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        CurrentHolder.removeCurrentId();
    }
}