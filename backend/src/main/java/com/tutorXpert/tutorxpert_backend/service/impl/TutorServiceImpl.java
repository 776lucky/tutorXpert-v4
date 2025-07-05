package com.tutorXpert.tutorxpert_backend.service.impl;

import com.tutorXpert.tutorxpert_backend.domain.po.Tutor;
import com.tutorXpert.tutorxpert_backend.mapper.TutorMapper;
import com.tutorXpert.tutorxpert_backend.service.ITutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TutorServiceImpl implements ITutorService {

    @Autowired
    private TutorMapper tutorMapper;

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
}
