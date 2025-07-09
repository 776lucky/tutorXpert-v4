package com.tutorXpert.tutorxpert_backend.service;

import com.tutorXpert.tutorxpert_backend.domain.dto.ProfileUpdateDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.Student;

import java.util.List;

public interface IStudentService {

    // 获取所有学生（后台、测试用）
    List<Student> getAllStudents();

    // 根据 ID 获取 Student
    Student getStudentById(Long id);

    // 新增 Student（开发、测试用）
    Student createStudent(Student student);

    // 删除 Student（后台管理）
    void deleteStudentById(Long id);

    // ✅ 学生用户更新个人资料（对外接口）
    void updateStudentProfile(Long userId, ProfileUpdateDTO payload);
}
