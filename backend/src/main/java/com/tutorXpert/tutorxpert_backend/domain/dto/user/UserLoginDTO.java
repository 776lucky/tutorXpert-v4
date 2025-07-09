package com.tutorXpert.tutorxpert_backend.domain.dto.user;

import lombok.Data;

@Data
public class UserLoginDTO {
    private Long id;
    private String email;
    private String role;
    private String name;
}
