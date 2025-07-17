package com.tutorXpert.tutorxpert_backend.domain.dto.profile;

import lombok.Data;

@Data
public class TutorProfileUpdateDTO {
    private String name;
    private String email;
//    private String password;
    private String bio;
    private String expertise;
    private Integer hourlyRate;
    private Integer yearsOfExperience;
    private String certifications;
    private String address;  // 公共字段
    private String avatarUrl;       // 头像，用于前端展示
    private String teachingModes;   // 授课方式（线上/线下）
    private String tags;            // 教师标签
    private String phoneNumber;
}