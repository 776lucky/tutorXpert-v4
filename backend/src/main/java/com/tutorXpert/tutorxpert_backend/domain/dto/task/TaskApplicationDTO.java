package com.tutorXpert.tutorxpert_backend.domain.dto.task;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskApplicationDTO {
    private Long id;
    private Long taskId;
    private Long tutorId;
    private Integer bidAmount;
    private String message;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
