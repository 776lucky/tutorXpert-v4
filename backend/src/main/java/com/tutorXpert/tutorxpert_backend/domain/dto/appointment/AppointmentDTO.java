package com.tutorXpert.tutorxpert_backend.domain.dto.appointment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDTO {
    private Long id;
    private Long tutorId;
    private Long studentId;
    private String subject;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String message;
    private String status;
}