package com.tutorXpert.tutorxpert_backend.service.impl;

import com.tutorXpert.tutorxpert_backend.domain.po.Appointment;
import com.tutorXpert.tutorxpert_backend.mapper.AppointmentMapper;
import com.tutorXpert.tutorxpert_backend.service.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentServiceImpl implements IAppointmentService {
    @Autowired
    private AppointmentMapper appointmentMapper;

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentMapper.selectList(null);
    }


    @Override
    public Appointment createAppointment(Appointment appointment) {
        // ✅ 打印当前实体类来源
        System.out.println("Class: " + Appointment.class.getCanonicalName());

        // ✅ 打印实体类所有字段
        for (var field : Appointment.class.getDeclaredFields()) {
            System.out.println("Field: " + field.getName());
        }

        appointment.setAppointmentTime(LocalDateTime.now());
        appointmentMapper.insert(appointment);
        return appointment;
    }
}
