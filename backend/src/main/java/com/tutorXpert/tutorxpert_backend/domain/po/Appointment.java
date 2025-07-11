package com.tutorXpert.tutorxpert_backend.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("appointments")
public class Appointment {
    @TableId(type = IdType.AUTO)       private Long id;

    private Long studentId;            // 当前登录学生
    private Long tutorId;              // 被预约家教
    private LocalDateTime startTime;   // 预约开始
    private LocalDateTime endTime;     // 预约结束（可选）
    private String subject;            // 科目
    private String message;            // 留言
    private String status;             // Pending / Confirmed / Rejected
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}