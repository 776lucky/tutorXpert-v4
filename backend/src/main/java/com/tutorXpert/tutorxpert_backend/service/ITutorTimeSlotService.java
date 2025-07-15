package com.tutorXpert.tutorxpert_backend.service;

import com.tutorXpert.tutorxpert_backend.domain.dto.AvailableSlots.TimeSlotDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.AvailableSlots.TutorTimeSlotDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.AvailableSlots.TutorTimeSlotDisplayDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.AvailableSlots.TutorTimeSlotRequestDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ITutorTimeSlotService {

    /** 获取所有 15 分钟模板格子 */
    List<TimeSlotDTO> getAllTimeSlots();

    /** 家教设置可用格子 */
    Map<String, Object> setTutorTimeSlots(Long tutorId, TutorTimeSlotRequestDTO dto);

    /** 查询某家教指定日期的可用格子 */
    List<TimeSlotDTO> getTutorSlotsByDate(Long tutorId, LocalDate date);

    /** （可选）取消家教某日某格子可用 */
    void removeTutorTimeSlots(Long tutorId, TutorTimeSlotRequestDTO dto);
}