package com.tutorXpert.tutorxpert_backend.controller;

import com.tutorXpert.tutorxpert_backend.domain.po.AvailableSlot;
import com.tutorXpert.tutorxpert_backend.service.IAvailableSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/availability")
public class AvailableSlotController {

    @Autowired
    private IAvailableSlotService availableSlotService;

    @GetMapping
    public List<AvailableSlot> getAllSlots() {
        return availableSlotService.getAllSlots();
    }

    @PostMapping
    public AvailableSlot addSlot(@RequestBody AvailableSlot slot) {
        return availableSlotService.addSlot(slot);
    }
}