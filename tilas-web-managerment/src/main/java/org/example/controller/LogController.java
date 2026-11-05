package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.pojo.Result;
import org.example.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LogController {
    @Autowired
    private LogService logService;
    @GetMapping("/log/page")
    public Result page(Integer page, Integer pageSize){
        log.info("分页查询：{} {}",page,pageSize);
        return Result.success(logService.getPage(page,pageSize));
    }
}
