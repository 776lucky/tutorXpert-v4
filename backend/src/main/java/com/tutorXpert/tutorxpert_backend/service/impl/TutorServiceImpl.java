package com.tutorXpert.tutorxpert_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.AvailableSlots.TutorTimeSlotDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.profile.TutorProfileUpdateDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.tutor.TutorDetailDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.tutor.TutorMapSearchResultDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.user.ProfileUpdateDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.tutor.TutorProfileSearchPageDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.TimeSlot;
import com.tutorXpert.tutorxpert_backend.domain.po.Tutor;
import com.tutorXpert.tutorxpert_backend.domain.po.TutorTimeSlot;
import com.tutorXpert.tutorxpert_backend.mapper.TimeSlotMapper;
import com.tutorXpert.tutorxpert_backend.mapper.TutorMapper;
import com.tutorXpert.tutorxpert_backend.mapper.TutorTimeSlotMapper;
import com.tutorXpert.tutorxpert_backend.mapper.UserMapper;
import com.tutorXpert.tutorxpert_backend.service.ITutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TutorServiceImpl implements ITutorService {

    @Autowired
    private TutorMapper tutorMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TutorTimeSlotMapper tutorTimeSlotMapper;
    @Autowired
    private TimeSlotMapper timeSlotMapper;

    @Override
    public List<Tutor> getAllTutors() {
        return tutorMapper.selectList(null);
    }

    @Override
    public Tutor getTutorById(Long id) {
        return tutorMapper.selectById(id);
    }

    @Override
    public Tutor createTutor(Tutor tutor) {
        tutorMapper.insert(tutor);
        return tutor;
    }

    @Override
    public void deleteTutorById(Long id) {
        tutorMapper.deleteById(id);
    }

    @Override
    public List<TutorProfileSearchPageDTO> searchTutors(double north, double south, double east, double west) {
        return userMapper.searchTutorProfiles(north, south, east, west);
    }


    @Override
    public List<TutorMapSearchResultDTO> searchTutorsByMapBounds(double north, double south, double east, double west) {
        return tutorMapper.searchTutorsByMapBounds(north, south, east, west);
    }


    @Override
    public void updateTutorProfile(Long userId, ProfileUpdateDTO payload) {

        // 查不到才插入，查到就更新
        Tutor tutor = tutorMapper.selectOne(new QueryWrapper<Tutor>().eq("user_id", userId));
        if (tutor == null) {
            // 确认真的不存在再插入
            tutor = new Tutor();
            tutor.setUserId(userId);
            TutorProfileUpdateDTO t = payload.getTutorProfile();
            if (t != null) {
                tutor.setBio(t.getBio());
                tutor.setExpertise(t.getExpertise());
                tutor.setHourlyRate(t.getHourlyRate());
                tutor.setYearsOfExperience(t.getYearsOfExperience());
                tutor.setCertifications(t.getCertifications());
            }
            tutorMapper.insert(tutor);
        } else {
            TutorProfileUpdateDTO t = payload.getTutorProfile();
            if (t != null) {
                tutor.setBio(t.getBio());
                tutor.setExpertise(t.getExpertise());
                tutor.setHourlyRate(t.getHourlyRate());
                tutor.setYearsOfExperience(t.getYearsOfExperience());
                tutor.setCertifications(t.getCertifications());
            }
            tutorMapper.updateById(tutor);
        }
    }

    @Override
    public TutorDetailDTO getTutorDetailById(Long tutorId) {
        return tutorMapper.getTutorDetailById(tutorId);
    }

    @Override
    public List<TutorTimeSlotDTO> getTutorTimeSlots(Long tutorId) {
        List<TutorTimeSlot> slots = tutorTimeSlotMapper.selectList(
                new QueryWrapper<TutorTimeSlot>()
                        .eq("tutor_id", tutorId)
        );

        if (slots.isEmpty()) return List.of();

        // 获取所有 slotId → 时间段
        List<Long> slotIds = slots.stream().map(TutorTimeSlot::getSlotId).toList();
        Map<Long, TimeSlot> timeSlotMap = timeSlotMapper.selectBatchIds(slotIds)
                .stream().collect(Collectors.toMap(TimeSlot::getId, ts -> ts));

        return slots.stream().map(s -> {
            TimeSlot ts = timeSlotMap.get(s.getSlotId());
            TutorTimeSlotDTO dto = new TutorTimeSlotDTO();
            dto.setDate(s.getAvailableDate());
            dto.setStartTime(ts.getStartTime().toString());
            dto.setEndTime(ts.getEndTime().toString());
            return dto;
        }).toList();
    }
}
