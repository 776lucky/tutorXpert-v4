package com.tutorXpert.tutorxpert_backend.domain.dto.task;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskUpdateDTO {
    private String title;
    private String subject;
    private String description;
    private String address;
    private Double lat;
    private Double lng;
    private String budget;
    private LocalDateTime deadline;
}
