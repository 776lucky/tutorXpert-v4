package com.tutorXpert.tutorxpert_backend.domain.dto.profile;


import lombok.Data;

@Data
public class StudentProfileUpdateDTO {
    private String educationLevel;
    private String subjectNeed;
    private String addressArea;
    private String briefDescription;
}