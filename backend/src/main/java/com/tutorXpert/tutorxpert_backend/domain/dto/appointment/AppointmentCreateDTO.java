package com.tutorXpert.tutorxpert_backend.domain.dto.appointment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AppointmentCreateDTO {
    @NotNull
    private Long tutorId;

    @NotNull
    private LocalDate date;

    @NotEmpty
    private List<Long> slotIds;   // 15 分钟格子 ID 列表

    private String subject;       // 可选：科目
    private String message;       // 可选：留言
}
