package com.tutorXpert.tutorxpert_backend.domain.dto.AvailableSlots;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class TutorTimeSlotRequestDTO {
    private LocalDate availableDate;     // 某一天
    private List<Long> slotIds;          // 格子ID列表（从 time_slots 表中选）
}
