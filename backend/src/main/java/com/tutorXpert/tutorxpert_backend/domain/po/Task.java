package com.tutorXpert.tutorxpert_backend.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tasks")
public class Task {
    @TableId(type = IdType.AUTO)  // ✅ 明确指定主键自增
    private Long id;

    private Long userId;
    private String title;
    private String subject;
    private String description;
    private String address;
    private Double lat;
    private Double lng;
    private String budget;
    private LocalDateTime deadline;
    private String status;
    private Long acceptedTutorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
