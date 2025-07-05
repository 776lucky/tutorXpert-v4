package com.tutorXpert.tutorxpert_backend.service;

import com.tutorXpert.tutorxpert_backend.domain.dto.TutorProfileSearchPageDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.Tutor;
import java.util.List;

public interface ITutorService {
    List<Tutor> getAllTutors();
    Tutor getTutorById(Long id);
    Tutor createTutor(Tutor tutor);
    void deleteTutorById(Long id);

    // ✅ 改为返回 DTO
    List<TutorProfileSearchPageDTO> searchTutors(double north, double south, double east, double west);
}
