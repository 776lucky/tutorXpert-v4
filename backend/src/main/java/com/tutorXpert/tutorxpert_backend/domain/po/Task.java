package com.tutorXpert.tutorxpert_backend.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@TableName("tasks")
public class Task {

    @TableId(type = IdType.AUTO)
//    @JsonSerialize(using = ToStringSerializer.class) // ✅ 防止 JS 精度丢失
    private Long id;

//    @JsonSerialize(using = ToStringSerializer.class) // ✅ 推荐也加（被前端引用时安全）
    private Long userId;

//    @JsonSerialize(using = ToStringSerializer.class) // ✅ 可选：避免前端解析错误
    private Long acceptedTutorId;

    private String title;
    private String subject;
    private String description;
    private String address;
    private Double lat;
    private Double lng;
    private Integer budget;
    private LocalDateTime deadline;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
