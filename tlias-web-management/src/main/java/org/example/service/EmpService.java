package org.example.service;

import org.example.pojo.Emp;
import org.example.pojo.EmpQueryParam;
import org.example.pojo.LoginInfo;
import org.example.pojo.PageResult;

import java.util.List;

public interface EmpService {
    PageResult<Emp> page(EmpQueryParam empQueryParam);
    void add(Emp emp);
    void delete(List<Integer> ids);
    Emp get(Integer id);

    void update(Emp emp);

    List<Emp> list();

    LoginInfo login(Emp emp);
}
