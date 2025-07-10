package com.tutorXpert.tutorxpert_backend.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("task_applications")
public class TaskApplication {
    private Long id;
    private Long taskId;
    private Long tutorId;
    private Integer bidAmount;
    private String message;
    private String status;  // e.g., "Pending", "Accepted", "Rejected"
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
