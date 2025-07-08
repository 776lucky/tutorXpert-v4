// AuthController.java
package com.tutorXpert.tutorxpert_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.LoginRequestDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.UserLoginDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.Student;
import com.tutorXpert.tutorxpert_backend.domain.po.Tutor;
import com.tutorXpert.tutorxpert_backend.domain.po.User;
import com.tutorXpert.tutorxpert_backend.mapper.StudentMapper;
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
    private final StudentMapper studentMapper;

    @Autowired
    public AuthController(JwtUtil jwtUtil, UserMapper userMapper, TutorMapper tutorMapper, StudentMapper studentMapper) {
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.tutorMapper = tutorMapper;
        this.studentMapper = studentMapper;
    }

    @Operation(summary = "register", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/register")
    public Map<String, String> register(@RequestParam String email,
                                        @RequestParam String password,
                                        @RequestParam String role,
                                        @RequestParam String name) {
        // 插入 users 表
        User user = new User();
        user.setEmail(email);
        user.setHashedPassword(encoder.encode(password));
        user.setRole(role);
        user.setName(name);
        userMapper.insert(user);

        // 插入 tutors / students 表（按角色）
        if ("tutor".equals(role)) {
            Tutor tutor = new Tutor();
            tutor.setUserId(user.getId());
            tutor.setBio("");
            tutor.setExpertise("");
            tutor.setHourlyRate(0);
            tutor.setYearsOfExperience(0);
            tutor.setCertifications("");
            tutorMapper.insert(tutor);
        } else if ("student".equals(role)) {
            Student student = new Student();
            student.setUserId(user.getId());
            student.setEducationLevel("");
            student.setAddressArea("");
            student.setBriefDescription("");
            studentMapper.insert(student);
        }

        return Collections.singletonMap("message", "User registered successfully");
    }

    @Operation(summary = "login", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequestDTO payload) {
        String email = payload.getEmail();
        String password = payload.getPassword();

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (user != null && encoder.matches(password, user.getHashedPassword())) {
            String token = jwtUtil.generateToken(email);
            UserLoginDTO dto = new UserLoginDTO();
            BeanUtils.copyProperties(user, dto);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", dto);
            return response;
        }
        System.out.println("email = " + email);
        System.out.println("password = " + password);

        throw new RuntimeException("Invalid credentials");
    }
}
