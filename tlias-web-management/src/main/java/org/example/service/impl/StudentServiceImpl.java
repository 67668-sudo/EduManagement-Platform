package org.example.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.WebSocket.DisciplinaryNoticeWebSocket;
import org.example.exception.RepeatNoException;
import org.example.mapper.StudentMapper;
import org.example.pojo.*;
import org.example.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Transactional
    @Override
    public void updateStudentViolation(Integer id, Integer score) {
        studentMapper.updateStudentViolation(id, score);
        Student student = studentMapper.selectStudentById(id);
        Emp emp = studentMapper.selectEmpById(student.getClazzId());
        ViolationAlert violationAlert = new ViolationAlert();
        violationAlert.setStudentId(student.getId());
        violationAlert.setStudentName(student.getName());
        violationAlert.setMasterId(emp.getId());
        violationAlert.setViolationScore(score);
        violationAlert.setIsRead(0);
        violationAlert.setCreateTime(LocalDateTime.now());
        DisciplinaryNoticeWebSocket.sendMessage(student.getName()
                + "同学在"
                + violationAlert.getCreateTime()
                + "违纪扣除"
                + violationAlert.getViolationScore()
                + "分"
                + "请及时教导处理"
                ,violationAlert);
    }


}
