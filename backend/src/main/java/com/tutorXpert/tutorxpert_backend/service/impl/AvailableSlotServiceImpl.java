package com.tutorXpert.tutorxpert_backend.service.impl;


import com.tutorXpert.tutorxpert_backend.domain.po.AvailableSlot;
import com.tutorXpert.tutorxpert_backend.mapper.AvailableSlotMapper;
import com.tutorXpert.tutorxpert_backend.service.IAvailableSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvailableSlotServiceImpl implements IAvailableSlotService {
    @Autowired
    private AvailableSlotMapper availableSlotMapper;

    @Override
    public List<AvailableSlot> getAllSlots() {
        return availableSlotMapper.selectList(null);
    }

    @Override
    public AvailableSlot addSlot(AvailableSlot slot) {
        availableSlotMapper.insert(slot);
        return slot;
    }
}
