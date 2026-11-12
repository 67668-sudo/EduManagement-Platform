package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.pojo.ViolationAlert;

import java.util.List;

@Mapper
public interface ViolationAlertMapper {

    void insertViolationAlert(ViolationAlert violationAlert);

    @Select("SELECT * FROM violation_alert WHERE master_id = #{masterId} and is_read = 0")
    List<ViolationAlert> selectHasAlert(Integer masterId);

    @Delete("DELETE FROM violation_alert WHERE id = #{id} and is_read = 0")
    void deleteViolationAlert(ViolationAlert violationAlert);

}
