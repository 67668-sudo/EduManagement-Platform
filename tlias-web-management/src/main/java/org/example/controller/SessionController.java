//package org.example.controller;
//
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.example.pojo.Result;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//public class SessionController {
//
//
//    @GetMapping("/c1")
//    public Result cookie1(HttpServletResponse response){
//        response.addCookie(new Cookie("c1", "cookie1"));
//        return Result.success();
//    }
//
//    @GetMapping("/c2")
//    public Result cookie2(HttpServletRequest request){
//        Cookie[] cookies = request.getCookies();
//        for (Cookie cookie : cookies) {
//            if(cookie.getName().equals("c1"))
//            log.info("cookie: {}", cookie.getName());
//        }
//        return Result.success();
//    }
//}
