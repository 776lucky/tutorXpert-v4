package com.tutorXpert.tutorxpert_backend.domain.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}
