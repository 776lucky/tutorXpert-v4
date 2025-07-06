// UserController.java
package com.tutorXpert.tutorxpert_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.UserLocationDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.Profile;
import com.tutorXpert.tutorxpert_backend.domain.po.Tutor;
import com.tutorXpert.tutorxpert_backend.domain.po.User;
import com.tutorXpert.tutorxpert_backend.mapper.ProfileMapper;
import com.tutorXpert.tutorxpert_backend.mapper.TutorMapper;
import com.tutorXpert.tutorxpert_backend.mapper.UserMapper;
import com.tutorXpert.tutorxpert_backend.security.JwtUtil;
import com.tutorXpert.tutorxpert_backend.service.IUserService;
import com.tutorXpert.tutorxpert_backend.service.impl.ProfileServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserMapper userMapper;
    private final TutorMapper tutorMapper;
    private final JwtUtil jwtUtil;
    private final ProfileMapper profileMapper;


    @Autowired
    public UserController(UserMapper userMapper, TutorMapper tutorMapper, JwtUtil jwtUtil, ProfileMapper profileMapper) {
        this.userMapper = userMapper;
        this.tutorMapper = tutorMapper;
        this.jwtUtil = jwtUtil;
        this.profileMapper = profileMapper;
    }

    @Autowired
    private IUserService userService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /** 查询所有用户 */
    // @Operation(summary = "Get All Users", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /** 管理员创建新用户（需带 JWT） */
    @Operation(summary = "Create User (Admin Only)", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setHashedPassword(encoder.encode(user.getHashedPassword()));
        userService.createUser(user);
        return user;
    }


    @Operation(summary = "Get My Profile", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/profile")
    public Object getMyProfile(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        String email = jwtUtil.validateToken(token);
        if (email == null) {
            throw new RuntimeException("Invalid token");
        }

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Map<String, Object> profile = new HashMap<>();
        profile.put("user", user);

        if ("tutor".equals(user.getRole())) {
            Tutor tutor = tutorMapper.selectOne(new QueryWrapper<Tutor>().eq("user_id", user.getId()));
            profile.put("tutor", tutor);
        }

        return profile;
    }

    @PutMapping("/profile")
    @Operation(summary = "Update My Profile", security = @SecurityRequirement(name = "bearerAuth"))
    public Object updateMyProfile(@RequestHeader("Authorization") String token,
                                  @RequestBody Map<String, Object> payload) {
        token = token.replace("Bearer ", "");
        String email = jwtUtil.validateToken(token);
        if (email == null) {
            throw new RuntimeException("Invalid token");
        }

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // 更新 User（如果有需要）
        if (payload.get("address") != null) {
            user.setAddress((String) payload.get("address"));
        }
        userMapper.updateById(user);

        // 更新 Tutor 表（重点是 bio）
        if ("tutor".equals(user.getRole())) {
            Tutor tutor = tutorMapper.selectOne(new QueryWrapper<Tutor>().eq("user_id", user.getId()));
            if (tutor != null) {
                if (payload.get("bio") != null) tutor.setBio((String) payload.get("bio"));
                if (payload.get("expertise") != null) tutor.setExpertise((String) payload.get("expertise"));
                if (payload.get("hourly_rate") != null) tutor.setHourlyRate((Integer) payload.get("hourly_rate"));
                tutorMapper.updateById(tutor);
            }
        }

        Profile profile = profileMapper.selectOne(new QueryWrapper<Profile>().eq("user_id", user.getId()));
        if (profile != null) {
            if (payload.get("address") != null) profile.setAddress((String) payload.get("address"));
            if (payload.get("education_level") != null) profile.setEducationLevel((String) payload.get("education_level"));
            if (payload.get("phone_number") != null) profile.setPhoneNumber((String) payload.get("phone_number"));
            profileMapper.updateById(profile);
        }

        return Map.of("message", "Profile updated successfully");
    }


}
