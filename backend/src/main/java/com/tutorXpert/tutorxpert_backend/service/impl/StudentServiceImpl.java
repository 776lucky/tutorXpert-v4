package com.tutorXpert.tutorxpert_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.ProfileUpdateDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.Student;
import com.tutorXpert.tutorxpert_backend.mapper.StudentMapper;
import com.tutorXpert.tutorxpert_backend.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<Student> getAllStudents() {
        return studentMapper.selectList(null);
    }

    @Override
    public Student getStudentById(Long id) {
        return studentMapper.selectById(id);
    }

    @Override
    public Student createStudent(Student student) {
        studentMapper.insert(student);
        return student;
    }

    @Override
    public void deleteStudentById(Long id) {
        studentMapper.deleteById(id);
    }

    @Override
    public void updateStudentProfile(Long userId, ProfileUpdateDTO payload) {
        Student student = studentMapper.selectOne(new QueryWrapper<Student>().eq("user_id", userId));
        if (student == null) {
            student = new Student();
            student.setUserId(userId);
        }

        student.setEducationLevel(payload.getEducationLevel());
        student.setSubjectNeed(payload.getSubjectNeed());
        student.setAddressArea(payload.getAddressArea());
        student.setBriefDescription(payload.getBriefDescription());

        if (student.getId() == null) {
            studentMapper.insert(student);
        } else {
            studentMapper.updateById(student);
        }
    }
}
