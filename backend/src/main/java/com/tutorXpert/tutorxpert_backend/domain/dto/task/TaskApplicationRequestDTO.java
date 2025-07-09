package com.tutorXpert.tutorxpert_backend.domain.dto.task;

import lombok.Data;

@Data
public class TaskApplicationRequestDTO {
    private Long tutorId;
    private Integer bidAmount;
    private String message;
}
