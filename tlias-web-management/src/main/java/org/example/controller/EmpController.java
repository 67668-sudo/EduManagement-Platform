package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.pojo.Emp;
import org.example.pojo.EmpQueryParam;
import org.example.pojo.PageResult;
import org.example.pojo.Result;
import org.example.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {
    private final EmpService empService;
    @Autowired
    public EmpController(EmpService empService) {
        this.empService = empService;
    }
    @GetMapping
    public Result page(EmpQueryParam empQueryParam) {
        log.info("分页查询{}",empQueryParam);
        PageResult<Emp> pageResult = empService.page(empQueryParam);
        return Result.success(pageResult);
    }

    @PostMapping
    public Result add(@RequestBody Emp emp) {
        log.info("新增{}",emp);
        empService.add(emp);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids) {
        log.info("删除{}",ids);
        empService.delete(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result get(@PathVariable Integer id) {
        log.info("查询{}",id);
        Emp emp = empService.get(id);
        return Result.success(emp);
    }

    @PutMapping
    public Result update(@RequestBody Emp emp) {
        log.info("修改员工：{}",emp);
        empService.update(emp);
        return Result.success();
    }

    @GetMapping("/list")
    public Result list() {
        log.info("查询所有员工");
        return Result.success(empService.list());
    }

}
