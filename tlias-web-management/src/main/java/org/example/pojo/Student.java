package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    private Integer id;              // 主键ID
    private String name;             // 姓名
    private String no;               // 学号
    private Integer gender;          // 性别 (1:男, 2:女)
    private String phone;            // 手机号
    private Integer degree;          // 学历 (1:初中...6:博士)
    private String idCard;           // 身份证号
    private Integer isCollege;       // 是否院校学生 (1:是, 0:否)
    private String address;          // 联系地址
    private LocalDate graduationDate;// 毕业时间 (只包含年月日)

    private Integer violationCount;  // 违纪次数
    private Integer violationScore;  // 违纪扣分

    private Integer clazzId;         // 班级ID

    private LocalDateTime createTime;// 创建时间
    private LocalDateTime updateTime;// 修改时间

    private String clazzName;

}