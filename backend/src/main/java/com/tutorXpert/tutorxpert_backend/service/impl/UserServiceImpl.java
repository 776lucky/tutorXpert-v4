package com.tutorXpert.tutorxpert_backend.service.impl;

import com.tutorXpert.tutorxpert_backend.domain.dto.user.UserDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.User;
import com.tutorXpert.tutorxpert_backend.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tutorXpert.tutorxpert_backend.service.IUserService;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userMapper.selectList(null);
        return users.stream().map(user -> {
            UserDTO dto = new UserDTO();
            BeanUtils.copyProperties(user, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userMapper.selectById(id);
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        userMapper.insert(user);
        BeanUtils.copyProperties(user, userDTO);  // 回写 ID
        return userDTO;
    }

    @Override
    public void deleteUserById(Long id) {
        userMapper.deleteById(id);
    }
}
