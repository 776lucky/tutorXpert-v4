// TutorTimeSlot.java
package com.tutorXpert.tutorxpert_backend.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("tutor_time_slots")
public class TutorTimeSlot {
    private Long tutorId;
    private Long slotId;
    private LocalDate availableDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // 新增字段
    private Boolean booked;  // false 未预约，true 已预约
}
