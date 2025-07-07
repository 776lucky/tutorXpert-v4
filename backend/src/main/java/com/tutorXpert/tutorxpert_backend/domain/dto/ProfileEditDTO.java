package com.tutorXpert.tutorxpert_backend.domain.dto;

import lombok.Data;

@Data
public class ProfileEditDTO {
    // Profile 表
    private String address;
    private String educationLevel;
    private String phoneNumber;

    // Tutor 表
    private String bio;
    private String expertise;
    private Integer hourlyRate;
}
