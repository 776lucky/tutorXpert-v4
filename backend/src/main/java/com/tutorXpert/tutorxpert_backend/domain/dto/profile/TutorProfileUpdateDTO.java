package com.tutorXpert.tutorxpert_backend.domain.dto.profile;

import lombok.Data;

@Data
public class TutorProfileUpdateDTO {
    private String bio;
    private String expertise;
    private Integer hourlyRate;
    private Integer yearsOfExperience;
    private String certifications;
}