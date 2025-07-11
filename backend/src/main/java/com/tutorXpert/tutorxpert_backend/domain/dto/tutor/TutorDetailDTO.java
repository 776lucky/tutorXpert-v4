package com.tutorXpert.tutorxpert_backend.domain.dto.tutor;

import lombok.Data;

@Data
public class TutorDetailDTO {
    private Long tutorId;
    private String bio;
    private String expertise;
    private Integer hourlyRate;
    private Integer yearsOfExperience;
    private String certifications;
    private Double lat;
    private Double lng;
}
