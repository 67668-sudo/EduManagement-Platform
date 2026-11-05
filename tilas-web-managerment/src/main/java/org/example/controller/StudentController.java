package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.pojo.Result;
import org.example.pojo.Student;
import org.example.pojo.StudentQueryParam;
import org.example.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {
    private StudentService studentService;
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping()
    public Result getStudents(StudentQueryParam studentQueryParam) {
        log.info("getStudents: {}", studentQueryParam);
        return Result.success(studentService.getStudentInfo(studentQueryParam));
    }

    @DeleteMapping("/{ids}")
    public Result deleteStudent(@PathVariable List<Integer> ids) {
        log.info("deleteStudent: {}", ids);
        studentService.deleteStudent(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getStudent(@PathVariable Integer id) {
        log.info("getStudent: {}", id);
        return Result.success(studentService.getStudent(id));
    }

    @PostMapping()
    public Result addStudent(@RequestBody Student student) {
        log.info("addStudent: {}", student);
        studentService.addStudent(student);
        return Result.success();
    }

    @PutMapping()
    public Result updateStudent(@RequestBody Student student) {
        log.info("updateStudent: {}", student);
        studentService.updateStudent(student);
        return Result.success();
    }

    @PutMapping("/violation/{id}/{score}")
    public Result updateStudentViolation(@PathVariable Integer id, @PathVariable Integer score) {
        log.info("updateStudentViolation: {}", id);
        studentService.updateStudentViolation(id, score);
        return Result.success();
    }


}
