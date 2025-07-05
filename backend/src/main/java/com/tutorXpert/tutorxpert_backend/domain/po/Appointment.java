package com.tutorXpert.tutorxpert_backend.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("appointments")
public class Appointment {
    private Long id;
    private Long studentId;
    private Long tutorId;
    private Long slotId;
    private LocalDateTime appointmentTime;
    private String status;  // e.g., "Pending", "Confirmed", "Cancelled"
}