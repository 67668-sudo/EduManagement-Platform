package org.example.service;

import org.example.pojo.Clazz;
import org.example.pojo.ClazzQueryParam;
import org.example.pojo.PageResult;

import java.util.List;

public interface ClazzService {
    PageResult<Clazz> get_page_query(ClazzQueryParam clazzQueryParam) throws InterruptedException;

    void Delete_calzz(Integer id);

    void add_clazz(Clazz clazz);

    Clazz get_clazz(Integer id);

    void update_clazz(Clazz clazz);

    List<Clazz> list();
}
