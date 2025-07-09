package com.tutorXpert.tutorxpert_backend.domain.dto.user;

import com.tutorXpert.tutorxpert_backend.domain.dto.profile.StudentProfileUpdateDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.profile.TutorProfileUpdateDTO;
import lombok.Data;

@Data
public class ProfileUpdateDTO {
    private String address;  // 公共字段
    private TutorProfileUpdateDTO tutorProfile;  // 家教专属
    private StudentProfileUpdateDTO studentProfile;  // 学生专属
}
