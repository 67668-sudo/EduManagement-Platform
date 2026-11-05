package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.pojo.JobOption;
import org.example.pojo.Result;
import org.example.pojo.StudentOption;
import org.example.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/report")
@RestController
public class ReportController {
    private ReportService reportService;
    @Autowired
    public ReportController(ReportService reportService){
        this.reportService = reportService;
    }

    @GetMapping("/empJobData")
    public Result getEmpJobData(){
        log.info("调用员工人数");
        JobOption jobOption = reportService.getEmpJobData();
        return Result.success(jobOption);
    }

    @GetMapping("/empGenderData")
    public Result getEmpGenderData(){
        log.info("调用员工性别");
        List<Map<String,Object>> genderList = reportService.getEmpGenderData();
        return Result.success(genderList);
    }

    @GetMapping("/studentDegreeData")
    public Result getStudentDegreeData(){
        log.info("调用学员学位");
        List<Map<String,Object>> genderList = reportService.getStudentDegreeData();
        return Result.success(genderList);
    }

    @GetMapping("/studentCountData")
    public Result getStudentCountData(){
        log.info("调用学员人数");
        StudentOption studentOption = reportService.getStudentCountData();
        return Result.success(studentOption);
    }
}
