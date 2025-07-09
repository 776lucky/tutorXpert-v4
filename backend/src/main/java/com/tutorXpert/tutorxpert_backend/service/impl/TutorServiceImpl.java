package com.tutorXpert.tutorxpert_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.profile.TutorProfileUpdateDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.user.ProfileUpdateDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.tutor.TutorProfileSearchPageDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.Tutor;
import com.tutorXpert.tutorxpert_backend.mapper.TutorMapper;
import com.tutorXpert.tutorxpert_backend.mapper.UserMapper;
import com.tutorXpert.tutorxpert_backend.service.ITutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutorServiceImpl implements ITutorService {

    @Autowired
    private TutorMapper tutorMapper;

    @Autowired
    private UserMapper userMapper;

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
    public void updateTutorProfile(Long userId, ProfileUpdateDTO payload) {
        Tutor tutor = tutorMapper.selectOne(new QueryWrapper<Tutor>().eq("user_id", userId));
        if (tutor == null) {
            tutor = new Tutor();
            tutor.setUserId(userId);
        }
        TutorProfileUpdateDTO t = payload.getTutorProfile();  // 取子 DTO
        if (t != null) {
            tutor.setBio(t.getBio());
            tutor.setExpertise(t.getExpertise());
            tutor.setHourlyRate(t.getHourlyRate());
            tutor.setYearsOfExperience(t.getYearsOfExperience());
            tutor.setCertifications(t.getCertifications());
        }

        if (tutor.getId() == null) {
            tutorMapper.insert(tutor);
        } else {
            tutorMapper.updateById(tutor);
        }
    }
}
