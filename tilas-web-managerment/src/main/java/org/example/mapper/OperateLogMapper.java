package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.example.pojo.OperateLog;

@Mapper
public interface OperateLogMapper {

    @Insert("INSERT INTO operate_log (operate_emp_id, operate_time, class_name, method_name, method_params, return_value,cost_time) VALUES (#{operateEmpId}, #{operateTime}, #{className}, #{methodName}, #{methodParams}, #{returnValue}, #{costTime})")
    void insert(OperateLog operateLog);
}
