package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.OperateLog;

import java.util.List;

@Mapper
public interface LogMapper {

    @Select("SELECT e.name as operateEmpName,o.* FROM operate_log as o left join emp as e on o.operate_emp_id = e.id")
    List<OperateLog> getAllLogs();
}
