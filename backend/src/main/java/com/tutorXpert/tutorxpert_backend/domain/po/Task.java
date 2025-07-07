package com.tutorXpert.tutorxpert_backend.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data                       // ✅ 自动生成 getter/setter/toString/equals/hashCode
@NoArgsConstructor          // ✅ 无参构造器
@AllArgsConstructor         // ✅ 全参构造器
@TableName("tasks")
public class Task {
    private Long id;
    private String title;
    private String subject;
    private String address;
    private String description;
    private String budget;
    private String deadline;
    private Double lat;
    private Double lng;
}
