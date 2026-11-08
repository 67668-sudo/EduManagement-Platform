package org.example.service;

import org.example.pojo.*;

import java.util.List;

public interface EmpService {
    PageResult<Emp> page(EmpQueryParam empQueryParam);
    void add(Emp emp);
    void delete(List<Integer> ids);
    Emp get(Integer id);

    void update(Emp emp);

    List<Emp> list();

    LoginInfo login(Emp emp);

    String sendVerifyCode(String phone);

    LoginInfo loginByPhone(Emp emp);
}
