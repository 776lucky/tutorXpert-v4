package com.tutorXpert.tutorxpert_backend.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@TableName("tutors")

public class Tutor {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;              // 外键关联 users.id
    private String bio;               // 简介
    private String expertise;         // 擅长领域
    private Integer hourlyRate;       // 时薪
    private Integer yearsOfExperience; // 教学经验年数
    private String certifications;    // 证书（可存 JSON 字符串或文本）
}

