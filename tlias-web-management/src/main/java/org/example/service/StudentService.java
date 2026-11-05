package org.example.service;

import org.example.pojo.PageResult;
import org.example.pojo.Student;
import org.example.pojo.StudentQueryParam;

import java.util.List;

public interface StudentService {
    PageResult<Student> getStudentInfo(StudentQueryParam studentQueryParam);

    void deleteStudent(List<Integer> id);

    void addStudent(Student student);

    Student getStudent(Integer id);

    void updateStudent(Student student);

    void updateStudentViolation(Integer id, Integer score);
}
