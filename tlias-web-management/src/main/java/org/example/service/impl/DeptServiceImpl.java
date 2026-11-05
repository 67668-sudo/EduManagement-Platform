package org.example.service.impl;
import org.example.mapper.DeptMapper;
import org.example.pojo.Dept;
import org.springframework.stereotype.Service;
import org.example.service.DeptService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService{
    private final DeptMapper deptMapper;
    public DeptServiceImpl(DeptMapper deptMapper){
        this.deptMapper = deptMapper;
    }
    @Override
    public List<Dept> findAll() {
        return deptMapper.findAll();
    }
    @Override
    public void delete(Integer id) {
        deptMapper.delete(id);
    }
    @Override
    public void add(Dept dept) {
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.add(dept);
    }
    @Override
    public void update(Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.update(dept);
    }

    @Override
    public Dept getById(Integer id) {
        return deptMapper.getById(id);
    }
}
