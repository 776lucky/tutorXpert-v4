package com.tutorXpert.tutorxpert_backend.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String email;
    private String hashedPassword;
    private String role;     // "tutor" or "student"
    private String name;
    private String address;
    private Double lat;
    private Double lng;
    private String avatarUrl;     // 用户头像 URL
    private String phoneNumber;
}

