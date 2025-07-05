package com.tutorXpert.tutorxpert_backend.service.impl;

import com.tutorXpert.tutorxpert_backend.domain.dto.TutorProfileSearchPageDTO;
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
    private UserMapper userMapper;  // ✅ 新增，用于地图家教搜索

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
        // 调用 UserMapper 的联表查询
        return userMapper.searchTutorProfiles(north, south, east, west);
    }

}
