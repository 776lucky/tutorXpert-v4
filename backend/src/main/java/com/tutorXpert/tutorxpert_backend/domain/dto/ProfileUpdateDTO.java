package com.tutorXpert.tutorxpert_backend.domain.dto;

import lombok.Data;

@Data
public class ProfileUpdateDTO {
    private String address;          // 通用 User 字段

    // Tutor 专属字段
    private String bio;
    private String expertise;
    private Integer hourlyRate;

    // Student 专属字段
    private String educationLevel;
    private String subjectNeed;
    private String addressArea;
    private String briefDescription;
}
