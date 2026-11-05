package org.example.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.exception.ExistStudentException;
import org.example.exception.NotExistEmpException;
import org.example.mapper.ClazzMapper;
import org.example.mapper.EmpMapper;
import org.example.mapper.StudentMapper;
import org.example.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.service.ClazzService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {
    private ClazzMapper clazzMapper;
    private EmpMapper empMapper;
    private StudentMapper studentMapper;

    @Autowired
    public void setEmpMapper(EmpMapper empMapper, ClazzMapper clazzMapper, StudentMapper studentMapper) {
        this.empMapper = empMapper;
        this.clazzMapper = clazzMapper;
        this.studentMapper = studentMapper;
    }
    @Override
    public PageResult<Clazz> get_page_query(ClazzQueryParam clazzQueryParam) {
        PageHelper.startPage(clazzQueryParam.getPage(), clazzQueryParam.getPageSize());
        List<Clazz> clazzList = clazzMapper.selectByQueryParams(clazzQueryParam);
        Page<Clazz> p = (Page<Clazz>) clazzList;
        return new PageResult<Clazz>(p.getTotal(),p.getResult());
    }
    @Transactional
    @Override
    public void Delete_calzz(Integer id) {
        Student student = studentMapper.selectStudentById(id);
        if (student != null) {
            throw new ExistStudentException("当前班级下还有学生，不能直接删除！");
        }
        empMapper.update_lose_job(id);
        clazzMapper.deleteClazz(id);
    }

    @Override
    public void add_clazz(Clazz clazz) {
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        if(clazz.getMasterId() != null){
            Emp emp = empMapper.search_job_by_id(clazz.getMasterId());
            if (emp == null) {
                throw new NotExistEmpException("不存在此员工，请先添加员工后再次尝试");
            }
        }
        clazzMapper.add_clazz(clazz);
    }

    @Override
    public Clazz get_clazz(Integer id) {
        return clazzMapper.get_clazz(id);
    }

    @Override
    public void update_clazz(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.update_clazz(clazz);
    }

    @Override
    public List<Clazz> list() {
        return clazzMapper.list();
    }

}
