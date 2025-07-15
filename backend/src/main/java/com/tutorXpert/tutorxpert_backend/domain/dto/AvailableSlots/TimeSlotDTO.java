package com.tutorXpert.tutorxpert_backend.domain.dto.AvailableSlots;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TimeSlotDTO {
    private Long id;
    private LocalDate date;
    private String startTime; // e.g. "08:00:00"
    private String endTime;   // e.g. "08:15:00"
}