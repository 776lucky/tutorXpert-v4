package com.tutorXpert.tutorxpert_backend.domain.dto.task;

import lombok.Data;

@Data
public class TaskSearchDTO {
    private Long id;
    private String title;
    private String subject;
    private String address;
    private Double lat;
    private Double lng;
}
