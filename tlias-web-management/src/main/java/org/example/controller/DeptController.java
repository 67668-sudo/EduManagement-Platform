package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.anno.LogOperation;
import org.example.pojo.Dept;
import org.example.pojo.Result;
import org.example.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class DeptController {

    private final DeptService deptService;
    @Autowired
    public DeptController(DeptService deptService){
        this.deptService = deptService;
    }
    @RequestMapping(value = "/depts",method = RequestMethod.GET)
    public Result list(){
//        System.out.println("查询全部的数据");
        log.info("查询全部的数据");
        List<Dept> deptList = deptService.findAll();
        return Result.success(deptList);
    }

    @LogOperation
    @DeleteMapping("/depts")
    public Result delete(Integer id){
//        System.out.println("删除id为"+id+"的数据");
        log.info("删除id为{}的数据", id);
        deptService.delete(id);
        return Result.success();
    }//通过前端url传参

    @LogOperation
    @PostMapping("/depts")
    public Result add(@RequestBody Dept dept){
//        System.out.println("添加数据"+dept);
        log.info("添加数据{}", dept);
        deptService.add(dept);
        return Result.success();
    }

    @LogOperation
    @PutMapping("/depts")
    public Result update(@RequestBody Dept dept) {
        deptService.update(dept);
        return Result.success();
    }

    @GetMapping("/depts/{id}")
    public Result getInfo(@PathVariable("id") Integer id){
//        System.out.println("查询id为"+id+"的数据");
        log.info("查询id为{}的数据", id);
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }
}
