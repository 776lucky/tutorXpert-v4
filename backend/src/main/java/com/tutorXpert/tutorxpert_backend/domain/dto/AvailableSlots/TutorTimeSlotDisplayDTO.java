package com.tutorXpert.tutorxpert_backend.domain.dto.AvailableSlots;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TutorTimeSlotDisplayDTO {
    private LocalDate date;              // 哪一天
    private String startTime;            // 格子起始时间，格式 HH:mm:ss
    private String endTime;              // 格子结束时间，格式 HH:mm:ss
}
