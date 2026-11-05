package org.example.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.pojo.Student;
import org.example.pojo.StudentQueryParam;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {

    @Select("select * from student where clazz_id = #{id} LIMIT 1")
    Student selectStudentById(Integer id);

    List<Student> getStudent(StudentQueryParam studentQueryParam);

    void deleteByIds(List<Integer> ids);

    void insert(Student student);

    @Select("select * from student where id = #{id}")
    Student selectStudent(Integer id);

    @Select("select * from student where no = #{no}")
    Student ifExist(Student student);

    @Select("select * from student where id != #{id} and no = #{no}")
    Student ifMyself(Student student);

    void updateStudent(Student student);

    @Update("update student set violation_count = violation_count + 1, violation_score = violation_score + #{score} where id = #{id}")
    void updateStudentViolation(Integer id, Integer score);

    List<Map<String, Object>> countStudentDegreeData();

    List<Map<String, Object>> countStudentCountData();
}
