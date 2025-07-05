package com.tutorXpert.tutorxpert_backend.domain.dto;

import lombok.Data;

@Data
public class TutorProfileSearchPageDTO {
    private Long id;
    private String email;
    private String role;
    private Double lat;
    private Double lng;
    private String address;
    private String bio;
    private String expertise;
    private Integer hourlyRate;
}

