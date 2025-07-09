package com.tutorXpert.tutorxpert_backend.domain.dto.tutor;

import lombok.Data;

@Data
public class TutorLocationDTO {
    private Long id;
    private String email;
    private Double lat;
    private Double lng;
    private String address;
}