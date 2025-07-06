// UserController.java
package com.tutorXpert.tutorxpert_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.UserLocationDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.Tutor;
import com.tutorXpert.tutorxpert_backend.domain.po.User;
import com.tutorXpert.tutorxpert_backend.mapper.TutorMapper;
import com.tutorXpert.tutorxpert_backend.mapper.UserMapper;
import com.tutorXpert.tutorxpert_backend.security.JwtUtil;
import com.tutorXpert.tutorxpert_backend.service.IUserService;
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

    @Autowired
    public UserController(UserMapper userMapper, TutorMapper tutorMapper, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.tutorMapper = tutorMapper;
        this.jwtUtil = jwtUtil;
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

    /**
     * 【接口名称】地图区域用户搜索接口
     * 【接口路径】GET /users/location
     * 【接口说明】根据地图边界和角色，筛选指定区域内用户，用于地图附近搜索
     * 【请求参数】
     *   - role：用户角色（tutor/student）
     *   - north, south, east, west：地图边界坐标
     * 【是否需要登录】否（公共接口）
     */
    @GetMapping("/location")
    public List<UserLocationDTO> searchUsersByLocation(
            @RequestParam String role,
            @RequestParam double north,
            @RequestParam double south,
            @RequestParam double east,
            @RequestParam double west) {
        List<User> users = userMapper.selectList(
                new QueryWrapper<User>()
                        .eq("role", role)
                        .between("lat", south, north)
                        .between("lng", west, east)
        );
        return users.stream().map(user -> {
            UserLocationDTO dto = new UserLocationDTO();
            BeanUtils.copyProperties(user, dto);
            return dto;
        }).collect(Collectors.toList());
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

}
