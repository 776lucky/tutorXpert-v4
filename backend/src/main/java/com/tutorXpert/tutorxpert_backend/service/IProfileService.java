package com.tutorXpert.tutorxpert_backend.service;

import com.tutorXpert.tutorxpert_backend.domain.po.Profile;

import java.util.List;


public interface IProfileService {
    List<Profile> getAllProfiles();
    Profile createProfile(Profile profile);
}
