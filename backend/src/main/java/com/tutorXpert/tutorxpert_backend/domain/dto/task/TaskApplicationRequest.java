package com.tutorXpert.tutorxpert_backend.domain.dto.task;

import lombok.Data;

@Data
public class TaskApplicationRequest {
    private Long tutorId;
    private Integer bidAmount;
    private String message;
}
