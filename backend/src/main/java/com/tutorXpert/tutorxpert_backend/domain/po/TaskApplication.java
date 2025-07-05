package com.tutorXpert.tutorxpert_backend.domain.po;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task_applications")
public class TaskApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long taskId;
    private Long tutorId;
    private Double bidAmount;
    private String message;
    private LocalDateTime appliedAt;
    private String status;  // e.g., "Pending", "Accepted", "Rejected"
}