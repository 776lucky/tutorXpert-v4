package com.tutorXpert.tutorxpert_backend.domain.dto.task;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDTO {
    private Long id;
    private Long userId;           // 发布人 ID
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

    // 可选：发布人信息（仅用于前端展示，后台可拼接后返回）
    private String studentName;
    private String studentEducationLevel;
}
