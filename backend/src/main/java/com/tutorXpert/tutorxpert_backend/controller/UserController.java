// UserController.java
package com.tutorXpert.tutorxpert_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.ProfileEditDTO;
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

    @GetMapping("/profile")
    @Operation(summary = "Get My Profile", security = @SecurityRequirement(name = "bearerAuth"))
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

        // 准备返回数据
        Map<String, Object> response = new HashMap<>();
        response.put("user", user);

        // 🔧 修复：无论什么角色都查询 tutor，但只有 tutor 角色才有数据
        Tutor tutor = tutorMapper.selectOne(new QueryWrapper<Tutor>().eq("user_id", user.getId()));
        response.put("tutor", tutor);  // 对于 student 用户，这里会是 null

        Profile profile = profileMapper.selectOne(new QueryWrapper<Profile>().eq("user_id", user.getId()));
        response.put("profile", profile);

        return response;
    }

    @PutMapping("/profile")
    @Operation(summary = "Update My Profile", security = @SecurityRequirement(name = "bearerAuth"))
    public Object updateMyProfile(@RequestHeader("Authorization") String token,
                                  @RequestBody ProfileEditDTO payload) {
        token = token.replace("Bearer ", "");
        String email = jwtUtil.validateToken(token);
        if (email == null) {
            throw new RuntimeException("Invalid token");
        }

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (payload.getAddress() != null) {
            user.setAddress(payload.getAddress());
        }
        userMapper.updateById(user);

        if ("tutor".equals(user.getRole())) {
            Tutor tutor = tutorMapper.selectOne(new QueryWrapper<Tutor>().eq("user_id", user.getId()));
            if (tutor != null) {
                if (payload.getBio() != null) tutor.setBio(payload.getBio());
                if (payload.getExpertise() != null) tutor.setExpertise(payload.getExpertise());
                if (payload.getHourlyRate() != null) tutor.setHourlyRate(payload.getHourlyRate());
                tutorMapper.updateById(tutor);
            }
        }

        Profile profile = profileMapper.selectOne(
                new QueryWrapper<Profile>().eq("user_id", user.getId())
        );
        if (profile == null) {
            profile = new Profile();
            profile.setUserId(user.getId());
            profileMapper.insert(profile);
        }

        if (payload.getAddress() != null) profile.setAddress(payload.getAddress());
        if (payload.getEducationLevel() != null) profile.setEducationLevel(payload.getEducationLevel());
        if (payload.getPhoneNumber() != null) profile.setPhoneNumber(payload.getPhoneNumber());
        profileMapper.updateById(profile);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Profile updated successfully");
        response.put("user", user);
        response.put("tutor", tutorMapper.selectOne(new QueryWrapper<Tutor>().eq("user_id", user.getId())));
        response.put("profile", profile);
        return response;
    }
}
