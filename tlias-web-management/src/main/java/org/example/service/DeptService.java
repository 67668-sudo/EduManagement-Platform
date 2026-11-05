package org.example.service;

import org.example.pojo.Dept;

import java.util.List;

public interface DeptService {
    List<Dept> findAll();
    void delete(Integer id);
    void add(Dept dept);
    void update(Dept dept);
    Dept getById(Integer id);

}
