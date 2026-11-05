package org.example.pojo;

import lombok.Data;

import java.time.LocalDate;

//name 否 黄埔一期 班级名称
//begin 否
//2023-01-
//        01
//范围匹配的开始时间(结课时间)
//end 否
//2023-05-
//        01
//范围匹配的结束时间(结课时间)
//page 是 1 分页查询的页码，如果未指定，默认为1
//pageSize 是 10
//分页查询的每页记录数，如果未指
@Data
public class ClazzQueryParam {
    private String name;
    private LocalDate begin;
    private LocalDate end;
    private int page;
    private int pageSize;
}
