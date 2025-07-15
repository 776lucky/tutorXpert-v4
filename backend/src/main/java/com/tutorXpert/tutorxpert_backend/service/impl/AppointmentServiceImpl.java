package com.tutorXpert.tutorxpert_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.appointment.AppointmentCreateDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.appointment.AppointmentDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.Appointment;
import com.tutorXpert.tutorxpert_backend.domain.po.TimeSlot;
import com.tutorXpert.tutorxpert_backend.domain.po.TutorTimeSlot;
import com.tutorXpert.tutorxpert_backend.mapper.AppointmentMapper;
import com.tutorXpert.tutorxpert_backend.mapper.TimeSlotMapper;
import com.tutorXpert.tutorxpert_backend.mapper.TutorTimeSlotMapper;
import com.tutorXpert.tutorxpert_backend.service.IAppointmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements IAppointmentService {

    @Autowired
    private AppointmentMapper appointmentMapper;
    @Autowired
    private TutorTimeSlotMapper tutorTimeSlotMapper;
    @Autowired
    private TimeSlotMapper timeSlotMapper;

    @Override
    @Transactional
    public AppointmentDTO createAppointment(Long studentId, AppointmentCreateDTO dto) {
        Appointment ap = new Appointment();
        BeanUtils.copyProperties(dto, ap);
        ap.setStudentId(studentId);
        ap.setStatus("Pending");
        ap.setCreatedAt(LocalDateTime.now());
        ap.setUpdatedAt(LocalDateTime.now());
        appointmentMapper.insert(ap);
        return toDTO(ap);
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByStudent(Long studentId) {
        return appointmentMapper.findByStudent(studentId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> getPendingAppointments(Long tutorId) {
        return appointmentMapper.findPendingByTutor(tutorId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AppointmentDTO updateStatus(Long tutorId, Long appointmentId, String action) {
        Appointment ap = appointmentMapper.selectById(appointmentId);
        if (ap == null || !ap.getTutorId().equals(tutorId)) {
            throw new RuntimeException("Appointment not found or no permission");
        }
        if ("accept".equalsIgnoreCase(action)) {
            ap.setStatus("Confirmed");
        } else if ("reject".equalsIgnoreCase(action)) {
            ap.setStatus("Rejected");
        } else {
            throw new IllegalArgumentException("Action must be accept or reject");
        }
        ap.setUpdatedAt(LocalDateTime.now());
        appointmentMapper.updateById(ap);
        return toDTO(ap);
    }


    // 1. 根据 dto.getDate() + dto.getSlotIds() 拆出具体时间段
    // 2. 校验这些 slotId 都在 tutor_time_slots 中且未被预约
    // 3. 对每个 slotId 插入一条 Appointment 记录
    @Override
    public void bookSlots(Long studentId, AppointmentCreateDTO dto) {
        LocalDate date = dto.getDate();
        Long tutorId = dto.getTutorId();
        List<Long> slotIds = dto.getSlotIds();

        // 1. 校验：这些 slotId 都在 tutor_time_slots 中（该 tutor 当天是否开放）
        List<TutorTimeSlot> available = tutorTimeSlotMapper.selectList(
                new QueryWrapper<TutorTimeSlot>()
                        .eq("tutor_id", tutorId)
                        .eq("available_date", date)
                        .in("slot_id", slotIds)
        );
        if (available.size() != slotIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some slots are not available for this tutor on " + date);
        }

        // 2. 查询对应的 TimeSlot 模板，并组装一个 Map<slotId, TimeSlot>
        List<TimeSlot> templates = timeSlotMapper.selectBatchIds(slotIds);
        Map<Long, TimeSlot> templateMap = templates.stream()
                .collect(Collectors.toMap(TimeSlot::getId, ts -> ts));

        // 3. 冲突检查：同一个 tutor 同一时间不能重复预约
        for (Long slotId : slotIds) {
            TimeSlot ts = templateMap.get(slotId);
            LocalDateTime start = LocalDateTime.of(date, ts.getStartTime().toLocalTime());
            long cnt = appointmentMapper.selectCount(new QueryWrapper<Appointment>()
                    .eq("tutor_id", tutorId)
                    .eq("start_time", start)
            );
            if (cnt > 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Slot at " + start + " is already booked");
            }
        }

        // 4. 插入所有预约记录
        LocalDateTime now = LocalDateTime.now();
        for (Long slotId : slotIds) {
            TimeSlot ts = templateMap.get(slotId);
            Appointment ap = new Appointment();
            ap.setStudentId(studentId);
            ap.setTutorId(tutorId);
            ap.setSubject(dto.getSubject());
            ap.setMessage(dto.getMessage());
            ap.setStartTime(LocalDateTime.of(date, ts.getStartTime().toLocalTime()));
            ap.setEndTime(LocalDateTime.of(date, ts.getEndTime().toLocalTime()));
            ap.setStatus("Pending");
            ap.setCreatedAt(now);
            ap.setUpdatedAt(now);
            appointmentMapper.insert(ap);

            // 2) 标记 booked = true
            tutorTimeSlotMapper.update(
                null,
                new UpdateWrapper<TutorTimeSlot>()
                    .set("booked", true)
                    .eq("tutor_id", tutorId)
                    .eq("available_date", date)
                    .eq("slot_id", slotId)
            );
        }
    }


    /* ============= private helper ============= */
    private AppointmentDTO toDTO(Appointment ap) {
        AppointmentDTO dto = new AppointmentDTO();
        BeanUtils.copyProperties(ap, dto);
        return dto;
    }
}
