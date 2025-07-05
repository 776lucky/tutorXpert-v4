package com.tutorXpert.tutorxpert_backend.service;

import com.tutorXpert.tutorxpert_backend.domain.po.AvailableSlot;

import java.util.List;

public interface IAvailableSlotService {
    List<AvailableSlot> getAllSlots();
    AvailableSlot addSlot(AvailableSlot slot);
}
