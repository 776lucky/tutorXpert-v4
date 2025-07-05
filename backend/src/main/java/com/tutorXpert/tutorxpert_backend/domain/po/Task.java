package com.tutorXpert.tutorxpert_backend.domain.po;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data                       // ✅ 自动生成 getter/setter/toString/equals/hashCode
@NoArgsConstructor          // ✅ 无参构造器
@AllArgsConstructor         // ✅ 全参构造器
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String subject;
    private String address;
    private String description;
    private String budget;
    private String deadline;
}
