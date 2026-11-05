//package org.example.filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//import java.io.IOException;
//
//@WebFilter(urlPatterns = "/*")   // 拦截所有请求
//public class DemoFilter implements Filter {
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        System.out.println("Filter 初始化");
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        System.out.println("Filter 前：请求进来了");
//        chain.doFilter(request, response);   // 👈 放行，交给下一个过滤器/Controller
//        System.out.println("Filter 后：响应回去了");
//    }
//
//    @Override
//    public void destroy() {
//        System.out.println("Filter 销毁");
//    }
//}