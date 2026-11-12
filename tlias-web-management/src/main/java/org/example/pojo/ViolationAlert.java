package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViolationAlert {
    private Integer id;
    private Integer studentId;
    private String studentName;
    private Integer masterId;
    private Integer violationScore;
    private Integer isRead; // 0-未读, 1-已读
    private LocalDateTime createTime;
}