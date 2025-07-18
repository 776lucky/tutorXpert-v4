package com.tutorXpert.tutorxpert_backend.domain.dto.profile;


import lombok.Data;

@Data
public class StudentProfileUpdateDTO {
    private String name;
    private String email;

    //    private String password;
    private String address;  // 公共字段
    private String avatarUrl;       // 头像，用于前端展示
    private String educationLevel;
    private String subjectNeed;
    private String addressArea;
    private String briefDescription;

}