package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//- total number 必须 总记录数
//|- rows
//        object
//        []
//必须 数据列表 item 类型:
//object
//|- id number 非必须 id
//|- name string 非必须 班级名称
//|- room string 非必须 班级教室
//|-
//beginDate
//string 非必须 开课时间
//|- endDate string 非必须 结课时间
//|-
//masterId
//number 非必须 班主任(员工ID)
//|-
//masterName
//string 非必须 班主任姓名(员工姓名)
//|-
//createTime
//string 非必须 创建时间
//|-
//updateTime
//string 非必须 更新时间
//|- status string 非必须 状态 （未开班、已开班、已
//结课）
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clazz {
    private Integer id;
    private String name;
    private String room;
    private String beginDate;
    private String endDate;
    private Integer masterId;
    private String masterName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String status;//不在数据库中，但是需要展示给前端
    private Integer subject;

}
