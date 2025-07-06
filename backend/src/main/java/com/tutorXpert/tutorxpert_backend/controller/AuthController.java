// AuthController.java
package com.tutorXpert.tutorxpert_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.UserLoginDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.Tutor;
import com.tutorXpert.tutorxpert_backend.domain.po.User;
import com.tutorXpert.tutorxpert_backend.mapper.TutorMapper;
import com.tutorXpert.tutorxpert_backend.mapper.UserMapper;
import com.tutorXpert.tutorxpert_backend.security.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final TutorMapper tutorMapper;

    @Autowired
    public AuthController(JwtUtil jwtUtil, UserMapper userMapper, TutorMapper tutorMapper) {
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.tutorMapper = tutorMapper;
    }

    @Operation(summary = "register", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/register")
    public Map<String, String> register(@RequestParam String email,
                                        @RequestParam String password,
                                        @RequestParam String role) {
        // 插入 users 表
        User user = new User();
        user.setEmail(email);
        user.setHashedPassword(encoder.encode(password));
        user.setRole(role);
        userMapper.insert(user);

        // 如果是 tutor，插入 tutors 表
        if ("tutor".equals(role)) {
            Tutor tutor = new Tutor();
            tutor.setUserId(user.getId());  // 确保 User 实体的 id 在插入后自动填充
            tutor.setBio("");  // 默认空简介
            tutor.setExpertise("");  // 默认空技能
            tutor.setHourlyRate(0);  // 默认时薪 0
            tutorMapper.insert(tutor);
        }

        return Collections.singletonMap("message", "User registered successfully");
    }

    @Operation(summary = "login", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (user != null && encoder.matches(password, user.getHashedPassword())) {
            String token = jwtUtil.generateToken(email);
            UserLoginDTO dto = new UserLoginDTO();
            BeanUtils.copyProperties(user, dto);  // PO 转 DTO
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", dto);
            return response;
        }
        throw new RuntimeException("Invalid credentials");
    }

}
