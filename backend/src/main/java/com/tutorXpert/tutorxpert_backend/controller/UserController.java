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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.tutorXpert.tutorxpert_backend.domain.po.Student;
import org.springframework.web.server.ResponseStatusException;

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
    @Operation(
            summary = "Get all users",
            description = "Get all users"
    )
    @GetMapping
    public List<User> getAllUsers() {
        return userMapper.selectList(null);
    }

    /** 管理员创建新用户 */
    @Operation(
            summary = "Admin create users",
            description = "Admin create users"
    )
    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setHashedPassword(encoder.encode(user.getHashedPassword()));
        userMapper.insert(user);
        return user;
    }

    /** 获取我的个人资料 */
    @Operation(
            summary = "获取我的个人资料",
            description = "需要在请求头中带上 Authorization Token",
            security = { @SecurityRequirement(name = "bearerAuth") }  // 👈 重点，标记需要 JWT
    )
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


    @Operation(
            summary = "修改我的个人资料",
            description = "需要在请求头中带上 Authorization Token，支持更新地址、家教或学生专属字段",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @PutMapping("/profile")
    public Map<String, Object> updateMyProfile(@RequestHeader("Authorization") String token,
                                               @RequestBody ProfileUpdateDTO payload) {

        // 解析 token，获取 email
        token = token.replace("Bearer ", "");
        String email = jwtUtil.validateToken(token);
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        // 查找当前用户
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        // 更新 User 表公共字段
        if (payload.getAddress() != null) {
            user.setAddress(payload.getAddress());

            // 地址转坐标（可选）
            Map<String, Double> coords = geocodeAddress(payload.getAddress());
            user.setLat(coords.get("lat"));
            user.setLng(coords.get("lng"));
        }
        userMapper.updateById(user);

        // 根据角色分别更新 tutors 或 students 表
        if ("tutor".equals(user.getRole())) {
            Tutor tutor = tutorMapper.selectOne(new QueryWrapper<Tutor>().eq("user_id", user.getId()));
            if (tutor == null) {
                tutor = new Tutor();
                tutor.setUserId(user.getId());
                // 设置字段
                tutor.setBio(payload.getBio());
                tutor.setExpertise(payload.getExpertise());
                tutor.setHourlyRate(payload.getHourlyRate());
                tutor.setYearsOfExperience(payload.getYearsOfExperience());
                tutor.setCertifications(payload.getCertifications());
                tutorMapper.insert(tutor);  // ✅ 插入新记录
            } else {
                // 更新字段
                tutor.setBio(payload.getBio());
                tutor.setExpertise(payload.getExpertise());
                tutor.setHourlyRate(payload.getHourlyRate());
                tutor.setYearsOfExperience(payload.getYearsOfExperience());
                tutor.setCertifications(payload.getCertifications());
                tutorMapper.updateById(tutor);  // ✅ 更新已有记录
            }
        } else if ("student".equals(user.getRole())) {
            Student student = studentMapper.selectOne(new QueryWrapper<Student>().eq("user_id", user.getId()));
            if (student == null) {
                student = new Student();
                student.setUserId(user.getId());
                student.setEducationLevel(payload.getEducationLevel());
                student.setSubjectNeed(payload.getSubjectNeed());
                student.setAddressArea(payload.getAddressArea());
                student.setBriefDescription(payload.getBriefDescription());
                studentMapper.insert(student);  // ✅ 插入新记录
            } else {
                student.setEducationLevel(payload.getEducationLevel());
                student.setSubjectNeed(payload.getSubjectNeed());
                student.setAddressArea(payload.getAddressArea());
                student.setBriefDescription(payload.getBriefDescription());
                studentMapper.updateById(student);  // ✅ 更新已有记录
            }
        }

        return Map.of("message", "Profile updated successfully");
    }

    /** 地址转坐标逻辑（你可以替换真实实现） */
    private Map<String, Double> geocodeAddress(String address) {
        // TODO: 这里写真实的地理编码逻辑
        Map<String, Double> coords = new HashMap<>();
        coords.put("lat", -33.86);  // 示例：假设地址是 Sydney
        coords.put("lng", 151.20);
        return coords;
    }
}