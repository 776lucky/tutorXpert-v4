package com.tutorXpert.tutorxpert_backend.service.impl;

import com.tutorXpert.tutorxpert_backend.domain.po.Profile;
import com.tutorXpert.tutorxpert_backend.mapper.ProfileMapper;
import com.tutorXpert.tutorxpert_backend.service.IProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileServiceImpl implements IProfileService {
    @Autowired
    private ProfileMapper profileMapper;

    @Override
    public List<Profile> getAllProfiles() {
        return profileMapper.selectList(null);
    }

    @Override
    public Profile createProfile(Profile profile) {
        profileMapper.insert(profile);
        return profile;
    }
}
