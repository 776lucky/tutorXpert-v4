package com.tutorXpert.tutorxpert_backend.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("students")
public class Student {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;               // 外键关联 users.id
    private String educationLevel;     // 教育水平
    private String addressArea;        // 地址区域（模糊地址）
    private String briefDescription;   // 简要描述（选填）
}
