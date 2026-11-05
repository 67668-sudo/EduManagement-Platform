package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperateLog {
    private Integer id;              // 主键
    private Integer operateEmpId;    // 操作人ID
    private LocalDateTime operateTime; // 操作时间
    private String className;        // 类名
    private String methodName;       // 方法名
    private String methodParams;     // 请求参数
    private String returnValue;      // 返回值
    private Long costTime;           // 执行耗时 (单位: ms)

    // 👈 扩展字段：连表查出来的操作人姓名（数据库里没有这列）
    private String operateEmpName;
}