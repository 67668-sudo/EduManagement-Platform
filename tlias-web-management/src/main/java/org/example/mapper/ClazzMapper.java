package org.example.mapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.Clazz;
import org.example.pojo.ClazzQueryParam;
import org.example.pojo.Student;

import java.util.List;

@Mapper
public interface ClazzMapper {

    List<Clazz> selectByQueryParams(ClazzQueryParam clazzQueryParam);


    @Delete("delete from clazz where id = #{id}")
    void deleteClazz(Integer id);


    void add_clazz(Clazz clazz);

    @Select("select * from clazz where id = #{id}")
    Clazz get_clazz(Integer id);


    void update_clazz(Clazz clazz);

    @Select("select * from clazz")
    List<Clazz> list();
}
