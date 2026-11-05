package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.anno.LogOperation;
import org.example.pojo.Clazz;
import org.example.pojo.ClazzQueryParam;
import org.example.pojo.PageResult;
import org.example.pojo.Result;
import org.example.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/clazzs")
public class ClazzController {
    private ClazzService clazzService;
    @Autowired
    public ClazzController(ClazzService clazzService){
        this.clazzService = clazzService;
    }

    @GetMapping
    public Result page_query(ClazzQueryParam clazzQueryParam){
        log.info("分页查询{}",clazzQueryParam);
        PageResult<Clazz> clazz = clazzService.get_page_query(clazzQueryParam);
        return Result.success(clazz);
    }

    @DeleteMapping("/{id}")
    public Result Delete_calzz(@PathVariable Integer id){
        log.info("删除id为{}的数据", id);
        clazzService.Delete_calzz(id);
        return Result.success();
    }

    @PostMapping
    public Result add_clazz(@RequestBody Clazz clazz){
        log.info("新增{}",clazz);
        clazzService.add_clazz(clazz);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result get_clazz(@PathVariable Integer id){
        log.info("查询id为{}的数据", id);
        Clazz clazz = clazzService.get_clazz(id);
        return Result.success(clazz);
    }

    @PutMapping
    public Result update_clazz(@RequestBody Clazz clazz){
        log.info("修改{}",clazz);
        clazzService.update_clazz(clazz);
        return Result.success();
    }

    @GetMapping("/list")
    public Result list(){
        log.info("查询所有数据");
        List<Clazz> clazzs = clazzService.list();
        return Result.success(clazzs);
    }

}
