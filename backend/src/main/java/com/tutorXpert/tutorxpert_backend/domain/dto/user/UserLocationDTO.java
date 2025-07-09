package com.tutorXpert.tutorxpert_backend.domain.dto.user;

import lombok.Data;

@Data
public class UserLocationDTO {
    private Long id;
    private String email;
    private String role;
    private Double lat;
    private Double lng;
    private String address;
}
