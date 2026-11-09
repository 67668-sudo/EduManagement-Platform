package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.pojo.Emp;
import org.example.pojo.LoginInfo;
import org.example.pojo.Result;
import org.example.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private EmpService empService;

    // 兼容旧前端：原登录页请求 POST /login，新路径是 /login/password
    @PostMapping({"", "/password"})
    public Result loginByPassword(@RequestBody Emp emp){
        log.info("登录：{}",emp);
        LoginInfo loginInfo = empService.login(emp);
        if (loginInfo != null) {
            return Result.success(loginInfo);
        } else {
            return Result.error("用户名或密码错误");
        }
    }
    @PostMapping("/verification")
    public Result loginByVerification(@RequestBody Emp emp){
        String verifyCode = empService.sendVerifyCode(emp.getPhone());
        log.info("验证码已生成：{}", verifyCode);
        return Result.success();
    }

    @PostMapping("/phone")
    public Result loginByPhone(@RequestBody Emp emp){
        log.info("通过手机号{}登录",emp.getPhone());
        LoginInfo loginInfo = empService.loginByPhone(emp);
        if (loginInfo != null) {
            return Result.success(loginInfo);
        } else {
            return Result.error("手机号未注册");
        }
    }
}
