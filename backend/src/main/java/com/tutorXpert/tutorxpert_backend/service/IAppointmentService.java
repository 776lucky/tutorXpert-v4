package com.tutorXpert.tutorxpert_backend.service;

import com.tutorXpert.tutorxpert_backend.domain.dto.appointment.AppointmentCreateDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.appointment.AppointmentDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.Appointment;
import jakarta.validation.Valid;

import java.util.List;

public interface IAppointmentService {

    /** 学生创建预约 */
    AppointmentDTO createAppointment(Long studentId, AppointmentCreateDTO dto);

    /** 学生查看自己的全部预约 */
    List<AppointmentDTO> getAppointmentsByStudent(Long studentId);

    /** Tutor 查看待处理预约 */
    List<AppointmentDTO> getPendingAppointments(Long tutorId);

    /** Tutor 接受或拒绝预约 */
    AppointmentDTO updateStatus(Long tutorId, Long appointmentId, String action); // action = "accept"|"reject"

    void bookSlots(Long studentId, AppointmentCreateDTO dto);
}
