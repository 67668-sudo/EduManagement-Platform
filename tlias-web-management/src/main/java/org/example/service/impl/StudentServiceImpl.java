package org.example.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.exception.RepeatNoException;
import org.example.mapper.StudentMapper;
import org.example.pojo.PageResult;
import org.example.pojo.Student;
import org.example.pojo.StudentQueryParam;
import org.example.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private StudentMapper studentMapper;
    public StudentServiceImpl(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }
    @Override
    public PageResult<Student> getStudentInfo(StudentQueryParam studentQueryParam) {
        PageHelper.startPage(studentQueryParam.getPage(), studentQueryParam.getPageSize());
        List<Student> page = studentMapper.getStudent(studentQueryParam);
        Page<Student> pageResult = (Page<Student>) page;
        return new PageResult<>(pageResult.getTotal(), pageResult.getResult());
    }

    @Override
    public void deleteStudent(List<Integer> id) {
        studentMapper.deleteByIds(id);
    }

    @Override
    public void addStudent(Student student) {
        Student existStudent = studentMapper.ifExist(student);
        if (existStudent != null) {
            throw new RepeatNoException("学生编号重复");
        }
        studentMapper.insert(student);
    }

    @Override
    public Student getStudent(Integer id) {
        return studentMapper.selectStudent(id);
    }

    @Override
    public void updateStudent(Student student) {
        Student existStudent = studentMapper.ifMyself(student);
        if (existStudent != null) {
            throw new RepeatNoException("学生编号重复");
        }
        studentMapper.updateStudent(student);
    }

    @Override
    public void updateStudentViolation(Integer id, Integer score) {
        studentMapper.updateStudentViolation(id, score);
    }


}
