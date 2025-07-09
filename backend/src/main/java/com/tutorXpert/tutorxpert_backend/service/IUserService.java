package com.tutorXpert.tutorxpert_backend.service;

import com.tutorXpert.tutorxpert_backend.domain.dto.user.UserDTO;
import java.util.List;

public interface IUserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO createUser(UserDTO userDTO);
    void deleteUserById(Long id);
}
