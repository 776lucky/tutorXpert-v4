package com.tutorXpert.tutorxpert_backend.domain.dto.AvailableSlots;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TutorTimeSlotDTO {

    private LocalDate date;
    private String startTime;
    private String endTime;

}
