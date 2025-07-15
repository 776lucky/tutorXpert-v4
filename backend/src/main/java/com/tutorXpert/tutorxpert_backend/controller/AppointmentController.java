package com.tutorXpert.tutorxpert_backend.controller;

import com.tutorXpert.tutorxpert_backend.domain.dto.appointment.AppointmentCreateDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.appointment.AppointmentDTO;
import com.tutorXpert.tutorxpert_backend.security.JwtUtil;
import com.tutorXpert.tutorxpert_backend.service.IAppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired private IAppointmentService appointmentService;
    @Autowired private JwtUtil jwtUtil;

    /* 2.3 学生创建预约 */
    /**
     * 学生根据日期 + slotIds 预约 tutor
     * POST /appointments
     */
    @Operation(summary = "Book tutor slots (student)")
    @PostMapping
    public Map<String, String> create(
            @RequestBody @Valid AppointmentCreateDTO dto,
            @RequestHeader("Authorization") String auth) {

        Long studentId = jwtUtil.getUserIdFromToken(auth.substring(7)).longValue();
        appointmentService.bookSlots(studentId, dto);
        return Map.of("message", "Appointment(s) successfully booked");
    }

    /* 学生查看自己的预约 */
    @Operation(summary = "Get my appointments (student)")
    @GetMapping("/my")
    public List<AppointmentDTO> myAppointments(@RequestHeader("Authorization") String auth) {
        Long studentId = jwtUtil.getUserIdFromToken(auth.substring(7)).longValue();
        return appointmentService.getAppointmentsByStudent(studentId);
    }

    /* Tutor 查看待处理预约 */
    @Operation(summary = "Get pending appointments (tutor)")
    @GetMapping("/tutor/pending")
    public List<AppointmentDTO> pending(@RequestHeader("Authorization") String auth) {
        Long tutorId = jwtUtil.getUserIdFromToken(auth.substring(7)).longValue();
        return appointmentService.getPendingAppointments(tutorId);
    }

    /* Tutor 接受 / 拒绝 */
    @Operation(summary = "Accept / Reject appointment (tutor)")
    @PostMapping("/{id}/{action}")   // action = accept | reject
    public AppointmentDTO review(@PathVariable Long id,
                                 @PathVariable String action,
                                 @RequestHeader("Authorization") String auth) {
        Long tutorId = jwtUtil.getUserIdFromToken(auth.substring(7)).longValue();
        return appointmentService.updateStatus(tutorId, id, action);
    }
}
