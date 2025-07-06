package com.tutorXpert.tutorxpert_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.ProfileDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.Profile;
import com.tutorXpert.tutorxpert_backend.mapper.ProfileMapper;
import com.tutorXpert.tutorxpert_backend.service.IProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements IProfileService {

    @Autowired
    private ProfileMapper profileMapper;

    @Override
    public ProfileDTO getProfileByUserId(int userId) {
        LambdaQueryWrapper<Profile> query = new LambdaQueryWrapper<>();
        query.eq(Profile::getUserId, userId);
        Profile profile = profileMapper.selectOne(query);

        if (profile == null) {
            throw new RuntimeException("Profile not found");
        }

        ProfileDTO dto = new ProfileDTO();
        dto.setAddress(profile.getAddress());
        dto.setEducationLevel(profile.getEducationLevel());
        dto.setPhoneNumber(profile.getPhoneNumber());
        return dto;
    }
}
