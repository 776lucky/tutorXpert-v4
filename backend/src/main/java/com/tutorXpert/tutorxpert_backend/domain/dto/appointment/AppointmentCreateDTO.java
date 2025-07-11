package com.tutorXpert.tutorxpert_backend.domain.dto.appointment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentCreateDTO {
    @NotNull
    private Long tutorId;
    @NotNull
    private String subject;
    @NotNull
    private LocalDateTime startTime;
    private LocalDateTime endTime;     // 可选
    private String message;            // 可选
}