package com.tutorXpert.tutorxpert_backend.domain.po;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("available_slots")
public class AvailableSlot {
    private Long id;
    private Long tutorId;
    private String subject;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}