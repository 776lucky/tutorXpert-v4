// UserController.java
package com.tutorXpert.tutorxpert_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.ProfileUpdateDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.Tutor;
import com.tutorXpert.tutorxpert_backend.domain.po.User;
import com.tutorXpert.tutorxpert_backend.mapper.StudentMapper;
import com.tutorXpert.tutorxpert_backend.mapper.TutorMapper;
import com.tutorXpert.tutorxpert_backend.mapper.UserMapper;
import com.tutorXpert.tutorxpert_backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.tutorXpert.tutorxpert_backend.domain.po.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserMapper userMapper;
    private final TutorMapper tutorMapper;
    private final StudentMapper studentMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public UserController(UserMapper userMapper, TutorMapper tutorMapper,
                          StudentMapper studentMapper, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.tutorMapper = tutorMapper;
        this.studentMapper = studentMapper;
        this.jwtUtil = jwtUtil;
    }

    /** 查询所有用户 */
    @GetMapping
    public List<User> getAllUsers() {
        return userMapper.selectList(null);
    }

    /** 管理员创建新用户 */
    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setHashedPassword(encoder.encode(user.getHashedPassword()));
        userMapper.insert(user);
        return user;
    }

    /** 获取我的个人资料 */
    @GetMapping("/profile")
    public Map<String, Object> getMyProfile(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        String email = jwtUtil.validateToken(token);
        if (email == null) {
            throw new RuntimeException("Invalid token");
        }

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("user", user);

        if ("tutor".equals(user.getRole())) {
            Tutor tutor = tutorMapper.selectOne(new QueryWrapper<Tutor>().eq("user_id", user.getId()));
            response.put("tutor", tutor);
        } else if ("student".equals(user.getRole())) {
            Student student = studentMapper.selectOne(new QueryWrapper<Student>().eq("user_id", user.getId()));
            response.put("student", student);
        }

        return response;
    }

    /** 修改我的个人资料 */
    @PutMapping("/profile")
    public Map<String, Object> updateMyProfile(@RequestHeader("Authorization") String token,
                                               @RequestBody ProfileUpdateDTO payload) {
        token = token.replace("Bearer ", "");
        String email = jwtUtil.validateToken(token);
        if (email == null) {
            throw new RuntimeException("Invalid token");
        }

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // 更新 UserPO
        if (payload.getAddress() != null) {
            user.setAddress(payload.getAddress());
        }
        userMapper.updateById(user);

        // 更新 TutorPO 或 StudentPO
        if ("tutor".equals(user.getRole())) {
            Tutor tutor = tutorMapper.selectOne(new QueryWrapper<Tutor>().eq("user_id", user.getId()));
            if (tutor != null) {
                if (payload.getBio() != null) tutor.setBio(payload.getBio());
                if (payload.getExpertise() != null) tutor.setExpertise(payload.getExpertise());
                if (payload.getHourlyRate() != null) tutor.setHourlyRate(payload.getHourlyRate());
                tutorMapper.updateById(tutor);
            }
        } else if ("student".equals(user.getRole())) {
            Student student = studentMapper.selectOne(new QueryWrapper<Student>().eq("user_id", user.getId()));
            if (student != null) {
                if (payload.getEducationLevel() != null) student.setEducationLevel(payload.getEducationLevel());
                if (payload.getAddressArea() != null) student.setAddressArea(payload.getAddressArea());
                if (payload.getBriefDescription() != null) student.setBriefDescription(payload.getBriefDescription());
                studentMapper.updateById(student);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Profile updated successfully");
        response.put("user", user);
        return response;
    }
}
