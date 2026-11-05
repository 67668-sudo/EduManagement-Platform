package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpExpr {

    private Integer id;          // 主键ID
    private Integer empId;       // 关联的员工ID (对应数据库 emp_id)
    private LocalDate begin;
    private LocalDate end;
    private String company;      // 公司
    private String job;          // 职位
}