package com.tutorXpert.tutorxpert_backend.controller;

import com.tutorXpert.tutorxpert_backend.domain.dto.AvailableSlots.TimeSlotDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.AvailableSlots.TutorTimeSlotDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.AvailableSlots.TutorTimeSlotRequestDTO;
import com.tutorXpert.tutorxpert_backend.security.JwtUtil;
import com.tutorXpert.tutorxpert_backend.service.ITutorTimeSlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tutor_time_slots")
public class TutorTimeSlotController {

    @Autowired
    private ITutorTimeSlotService service;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 1. 获取所有 15 分钟模板格子
     * GET /tutor_time_slots/template
     */
    @Operation(
            summary = "Get all 15-minute time slot templates",
            description = "Returns a list of predefined 15-minute time slots that tutors can use to set their availability."
    )
    @GetMapping("/template")
    public List<TimeSlotDTO> getTemplateSlots() {
        return service.getAllTimeSlots();
    }

    /**
     * 2. 家教设置可用格子
     * POST /tutor_time_slots
     */
    @Operation(
            summary = "Set available time slots for the current tutor",
            description = "Allows a tutor to define their available time slots based on the provided DTO. "
                    + "Authentication is performed using the JWT token in the Authorization header."
    )
    @PostMapping
    public Map<String, Object> setMySlots(
            @RequestBody @Valid TutorTimeSlotRequestDTO dto,
            @RequestHeader("Authorization") String auth) {

        Long tutorId = jwtUtil.getUserIdFromToken(auth.substring(7)).longValue();
        return service.setTutorTimeSlots(tutorId, dto);
    }

    /**
     * 3. 查询自己某日可用格子
     * GET /tutor_time_slots/my?date=2025-07-20
     */
    @Operation(
            summary = "Retrieve available time slots for the current tutor on a specific date",
            description = "Fetches the list of time slots that the tutor has marked as available for a given date. "
                    + "The date must be provided as a query parameter. Authentication is via JWT token."
    )
    @GetMapping("/my")
    public List<TimeSlotDTO> getMySlots(
            @RequestParam("date") String dateStr,
            @RequestHeader("Authorization") String auth) {

        Long tutorId = jwtUtil.getUserIdFromToken(auth.substring(7)).longValue();
        LocalDate date = LocalDate.parse(dateStr);
        return service.getTutorSlotsByDate(tutorId, date);
    }



    /**
     * （可选）取消某日部分格子
     * DELETE /tutor_time_slots
     */

    @Operation(
            summary = "Remove selected time slots for the current tutor",
            description = "Allows a tutor to remove some of their previously set available time slots. "
                    + "Authentication is via JWT token in the Authorization header."
    )
    @DeleteMapping
    public void removeMySlots(
            @RequestBody @Valid TutorTimeSlotRequestDTO dto,
            @RequestHeader("Authorization") String auth) {

        Long tutorId = jwtUtil.getUserIdFromToken(auth.substring(7)).longValue();
        service.removeTutorTimeSlots(tutorId, dto);
    }
}