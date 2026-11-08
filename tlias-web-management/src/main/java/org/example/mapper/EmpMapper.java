package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.pojo.Emp;
import org.example.pojo.EmpQueryParam;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {
//    @Select("select e.*,d.name as dept_name from emp as e left join dept as d on e.dept_id = d.id")
//    List<Emp> getAllEmp();
//    古法分页查询
//    @Select("select count(*) from emp as e left outer join dept as d on e.dept_id = d.id")
//    public long count();
//
//    @Select("select e.*,d.name as deptName from emp as e left join dept as d on e.dept_id = d.id order by e.update_time desc limit #{startIndex},#{pageSize}")
//    public List<Emp> list(Integer startIndex, Integer pageSize);//分页查询

//    @Select("select e.*,d.name as dept_name from emp as e left join dept as d on e.dept_id = d.id order by e.update_time desc")
    List<Emp> list(EmpQueryParam empQueryParam);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into emp (username, password, name, gender, phone, job, salary, image, entry_date, dept_id) " +
            "values (#{username}, #{password}, #{name}, #{gender}, #{phone}, #{job}, #{salary}, #{image}, #{entryDate}, #{deptId})")
    void insert(Emp emp);


    void deleteByIds(List<Integer> ids);

    Emp getEmpById(Integer id);

    void updateById(Emp emp);

    List<Map<String, Object>> countEmpJobData();

    List<Map<String, Object>> countEmpGenderData();


    void update_lose_job(Integer id);

    @Select("select * from emp where id = #{masterId}")
    Emp search_job_by_id(Integer masterId);

    List<Emp> getEmpList();

    @Select("select * from emp where username = #{username} and password = #{password}")
    Emp selectByUsernameAndPassword(Emp emp);

    @Select("select * from emp where phone = #{phone}")
    Emp selectByPhone(String phone);
}
