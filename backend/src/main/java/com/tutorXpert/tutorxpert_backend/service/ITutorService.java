package com.tutorXpert.tutorxpert_backend.service;

import com.tutorXpert.tutorxpert_backend.domain.dto.tutor.TutorMapSearchResultDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.user.ProfileUpdateDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.tutor.TutorProfileSearchPageDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.Tutor;

import java.util.List;

public interface ITutorService {

    // 获取所有家教（管理后台、测试用）
    List<Tutor> getAllTutors();

    // 根据 ID 获取 Tutor
    Tutor getTutorById(Long id);

    // 新增 Tutor（通常只用于开发、测试，不对外暴露）
    Tutor createTutor(Tutor tutor);

    // 删除 Tutor（后台管理用）
    void deleteTutorById(Long id);

    // ✅ 搜索 Tutor（地图搜索页用，返回专用 DTO）
    List<TutorProfileSearchPageDTO> searchTutors(double north, double south, double east, double west);

    // ✅ Tutor 用户更新个人家教资料（对外接口）
    void updateTutorProfile(Long userId, ProfileUpdateDTO payload);

    // 地图专用接口（地图搜索页用，返回专用 DTO）
    List<TutorMapSearchResultDTO> searchTutorsByMapBounds(double north, double south, double east, double west);

}
