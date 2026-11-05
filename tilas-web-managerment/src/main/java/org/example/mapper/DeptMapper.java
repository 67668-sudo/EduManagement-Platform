package org.example.mapper;
import org.apache.ibatis.annotations.*;
import org.example.pojo.Dept;

import java.util.List;

@Mapper
public interface DeptMapper {
//    @Results({
//            @Result(column = "create_time", property = "createTime"),
//            @Result(column = "update_time", property = "updateTime")
//    })
    @Select("select * from dept")
    List<Dept> findAll();

    @Delete("delete from dept where id = #{id}")
    void delete(Integer id);

    @Insert("insert into dept(name,create_time,update_time) values(#{name},#{createTime},#{updateTime})")
    void add(Dept dept);

    @Select("select * from dept where id = #{id}")
    Dept getById(Integer id);

    @Update("update dept set name = #{name}, update_time = #{updateTime} where id = #{id}")
    void update(Dept dept);

}
