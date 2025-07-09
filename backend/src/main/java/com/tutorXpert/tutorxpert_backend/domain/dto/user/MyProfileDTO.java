package com.tutorXpert.tutorxpert_backend.domain.dto.user;

import lombok.Data;

@Data
public class MyProfileDTO {
    private UserLoginDTO user;   // 公共账户信息（id、email、role、name）
    private Object roleProfile;  // 家教/学生专属资料（TutorProfileDTO 或 StudentProfileDTO）
}
