// UserController.java
package com.tutorXpert.tutorxpert_backend.controller;

import com.tutorXpert.tutorxpert_backend.domain.po.User;
import com.tutorXpert.tutorxpert_backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /** 查询所有用户（需带 JWT） */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /** 管理员创建新用户（需带 JWT） */
    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setHashedPassword(encoder.encode(user.getHashedPassword()));
        userService.createUser(user);
        return user;
    }
}
