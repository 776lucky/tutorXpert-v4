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

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired private IAppointmentService appointmentService;
    @Autowired private JwtUtil jwtUtil;

    /* 2.3 学生创建预约 */
    @Operation(summary = "Create appointment (student)")
    @PostMapping
    public AppointmentDTO create(@RequestBody @Valid AppointmentCreateDTO dto,
                                 @RequestHeader("Authorization") String auth) {
        String raw = auth.substring(7);
        Long studentId = jwtUtil.getUserIdFromToken(raw).longValue();   // 方案 A
        return appointmentService.createAppointment(studentId, dto);
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
