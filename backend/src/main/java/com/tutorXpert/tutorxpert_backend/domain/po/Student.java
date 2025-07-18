package com.tutorXpert.tutorxpert_backend.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("students")
public class Student {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")  // 添加这行
    private Long userId;               // 外键关联 users.id
    private String educationLevel;     // 教育水平
    private String addressArea;        // 地址区域（模糊地址）
    private String briefDescription;   // 简要描述（选填）x`
    private String subjectNeed;     // 所需科目（你已有 setter，但字段未定义）
}
