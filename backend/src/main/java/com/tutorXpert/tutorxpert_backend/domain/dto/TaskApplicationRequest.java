package com.tutorXpert.tutorxpert_backend.domain.dto;

import lombok.Data;

@Data
public class TaskApplicationRequest {
    private Long tutorId;
    private Integer bidAmount;
    private String message;
}
