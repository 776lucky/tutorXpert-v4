package com.tutorXpert.tutorxpert_backend.service.impl;

import com.tutorXpert.tutorxpert_backend.domain.dto.appointment.AppointmentCreateDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.appointment.AppointmentDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.Appointment;
import com.tutorXpert.tutorxpert_backend.mapper.AppointmentMapper;
import com.tutorXpert.tutorxpert_backend.service.IAppointmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements IAppointmentService {

    @Autowired
    private AppointmentMapper appointmentMapper;

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

    /* ============= private helper ============= */
    private AppointmentDTO toDTO(Appointment ap) {
        AppointmentDTO dto = new AppointmentDTO();
        BeanUtils.copyProperties(ap, dto);
        return dto;
    }
}
