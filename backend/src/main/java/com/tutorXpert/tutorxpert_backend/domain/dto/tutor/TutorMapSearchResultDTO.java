package com.tutorXpert.tutorxpert_backend.domain.dto.tutor;

import lombok.Data;

@Data
public class TutorMapSearchResultDTO {
    private Long tutorId;
    private String bio;
    private String expertise;
    private Integer hourlyRate;
    private Double lat;
    private Double lng;
}
