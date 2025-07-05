package com.tutorXpert.tutorxpert_backend.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tutors")
public class Tutor {
    private Long id;
    private Long userId;
    private String bio;
    private String expertise;
    private Integer hourlyRate;
}
