package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Emp {

    // ================== 1. 基础字段（对应 emp 表） ==================
    private Integer id;              // 主键ID
    private String username;         // 用户名
    private String password = "123456";         // 密码
    private String name;             // 姓名
    private Integer gender;
    private String phone;            // 手机号
    private Short job;               // 职位 (1班主任, 2讲师...)
    private BigDecimal salary;       // 薪资 (金额必须用 BigDecimal)
    private String image;            // 头像URL
    private LocalDate entryDate;
    private Integer deptId;          // 部门ID
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private String deptName; // 部门

    private List<EmpExpr> exprList;

}