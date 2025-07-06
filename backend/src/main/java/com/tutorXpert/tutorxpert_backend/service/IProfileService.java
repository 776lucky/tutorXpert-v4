package com.tutorXpert.tutorxpert_backend.service;

import com.tutorXpert.tutorxpert_backend.domain.dto.ProfileDTO;

public interface IProfileService {
    ProfileDTO getProfileByUserId(int userId);
}