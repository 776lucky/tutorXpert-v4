package com.tutorXpert.tutorxpert_backend.domain.dto.task;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskSearchDTO {
    private Long id;
    private Long userId;
    private String title;
    private String subject;
    private String description;
    private String address;
    private Double lat;
    private Double lng;
    private Integer budget;
    private LocalDateTime deadline;
    private String status;
    private LocalDateTime createdAt;
}

