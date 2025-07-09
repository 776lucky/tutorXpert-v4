package com.tutorXpert.tutorxpert_backend.domain.dto.task;

import lombok.Data;

@Data
public class TaskApplicationDecisionDTO {
    private String status;  // "Accepted" or "Rejected"
}
