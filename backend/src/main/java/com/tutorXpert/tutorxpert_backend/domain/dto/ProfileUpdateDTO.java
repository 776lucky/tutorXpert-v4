package com.tutorXpert.tutorxpert_backend.domain.dto;

import lombok.Data;

@Data
public class ProfileUpdateDTO {
    private String address;

    // Tutor专属字段
    private String bio;
    private String expertise;
    private Integer hourlyRate;
    private Integer yearsOfExperience;
    private String certifications;

    // Student专属字段
    private String educationLevel;
    private String subjectNeed;
    private String addressArea;
    private String briefDescription;
}

