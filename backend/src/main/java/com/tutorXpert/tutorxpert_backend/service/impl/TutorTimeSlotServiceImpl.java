package com.tutorXpert.tutorxpert_backend.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.AvailableSlots.TimeSlotDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.AvailableSlots.TutorTimeSlotDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.AvailableSlots.TutorTimeSlotDisplayDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.AvailableSlots.TutorTimeSlotRequestDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.TutorTimeSlot;
import com.tutorXpert.tutorxpert_backend.mapper.TimeSlotMapper;
import com.tutorXpert.tutorxpert_backend.mapper.TutorTimeSlotMapper;
import com.tutorXpert.tutorxpert_backend.service.ITutorTimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TutorTimeSlotServiceImpl implements ITutorTimeSlotService {

    @Autowired
    private TimeSlotMapper timeSlotMapper;

    @Autowired
    private TutorTimeSlotMapper tutorTimeSlotMapper;

    @Override
    public List<TimeSlotDTO> getAllTimeSlots() {
        return timeSlotMapper.selectList(null).stream().map(po -> {
            TimeSlotDTO dto = new TimeSlotDTO();
            // 转成字符串，前端易处理
            dto.setId(po.getId());
            dto.setStartTime(po.getStartTime().toString());
            dto.setEndTime(po.getEndTime().toString());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> setTutorTimeSlots(Long tutorId, TutorTimeSlotRequestDTO dto) {
        // 先删除已有同日记录
        tutorTimeSlotMapper.delete(new QueryWrapper<TutorTimeSlot>()
                .eq("tutor_id", tutorId)
                .eq("available_date", dto.getAvailableDate()));

        LocalDateTime now = LocalDateTime.now();

        // 批量插入
        dto.getSlotIds().forEach(slotId -> {
            TutorTimeSlot po = new TutorTimeSlot();
            po.setTutorId(tutorId);
            po.setSlotId(slotId);
            po.setAvailableDate(dto.getAvailableDate());
            po.setCreatedAt(now);
            po.setUpdatedAt(now);
            po.setBooked(false);
            tutorTimeSlotMapper.insert(po);
        });

        // ✅ 返回标准响应体
        return Map.of("message", "Slots set successfully");
    }


    @Override
    public List<TimeSlotDTO> getTutorSlotsByDate(Long tutorId, LocalDate date) {
        // 先查关联表得到 slotIds
        List<Long> slotIds = tutorTimeSlotMapper.selectList(
                new QueryWrapper<TutorTimeSlot>()
                        .eq("tutor_id", tutorId)
                        .eq("available_date", date)
                        .eq("booked", false)
        ).stream().map(TutorTimeSlot::getSlotId).collect(Collectors.toList());

        // 再查模板表返回 DTO
        if (slotIds.isEmpty()) return List.of();
        return timeSlotMapper.selectBatchIds(slotIds).stream().map(po -> {
            TimeSlotDTO dto = new TimeSlotDTO();
            dto.setId(po.getId());
            dto.setStartTime(po.getStartTime().toString());
            dto.setEndTime(po.getEndTime().toString());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void removeTutorTimeSlots(Long tutorId, TutorTimeSlotRequestDTO dto) {
        tutorTimeSlotMapper.delete(new QueryWrapper<TutorTimeSlot>()
                .eq("tutor_id", tutorId)
                .eq("available_date", dto.getAvailableDate())
                .in("slot_id", dto.getSlotIds()));
    }
}