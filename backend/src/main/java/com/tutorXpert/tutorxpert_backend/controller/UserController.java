// UserController.java
package com.tutorXpert.tutorxpert_backend.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.profile.StudentProfileUpdateDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.profile.TutorProfileUpdateDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.user.MyProfileDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.user.ProfileUpdateDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.user.UserLocationDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.user.UserLoginDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.Tutor;
import com.tutorXpert.tutorxpert_backend.domain.po.User;
import com.tutorXpert.tutorxpert_backend.mapper.StudentMapper;
import com.tutorXpert.tutorxpert_backend.mapper.TutorMapper;
import com.tutorXpert.tutorxpert_backend.mapper.UserMapper;
import com.tutorXpert.tutorxpert_backend.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.tutorXpert.tutorxpert_backend.domain.po.Student;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            summary = "Get all users locations",
            description = "Get all users locations"
    )
    @GetMapping("/locations")
    public List<UserLocationDTO> getAllUserLocations() {
        List<User> users = userMapper.selectList(null);
        return users.stream().map(user -> {
            UserLocationDTO dto = new UserLocationDTO();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole());
            dto.setLat(user.getLat());
            dto.setLng(user.getLng());
            dto.setAddress(user.getAddress());
            return dto;
        }).toList();
    }

    @Operation(summary = "获取我的个人资料（Tutor）", security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/profile/tutor")
    public TutorProfileUpdateDTO getMyTutorProfile() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        if (!"tutor".equals(user.getRole())) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not a tutor");

        Tutor tutor = tutorMapper.selectOne(new QueryWrapper<Tutor>().eq("user_id", user.getId()));

        TutorProfileUpdateDTO dto = new TutorProfileUpdateDTO();
        // 填充 User 字段
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAddress(user.getAddress());
        dto.setAvatarUrl(user.getAvatarUrl());

        // 填充 Tutor 字段
        if (tutor != null) {
            dto.setBio(tutor.getBio());
            dto.setExpertise(tutor.getExpertise());
            dto.setHourlyRate(tutor.getHourlyRate());
            dto.setYearsOfExperience(tutor.getYearsOfExperience());
            dto.setCertifications(tutor.getCertifications());
            dto.setTeachingModes(tutor.getTeachingModes());
            dto.setTags(tutor.getTags());
        }

        return dto;
    }


    @Operation(summary = "更新我的个人资料（Tutor）", security = { @SecurityRequirement(name = "bearerAuth") })
    @PostMapping("/profile/tutor")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMyTutorProfile(@RequestBody @Valid TutorProfileUpdateDTO dto) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        if (!"tutor".equals(user.getRole())) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not a tutor");

        // 更新 users 表
        user.setName(dto.getName());
        user.setAddress(dto.getAddress());
        user.setAvatarUrl(dto.getAvatarUrl());
        user.setPhoneNumber(dto.getEmail());
        userMapper.updateById(user);

        // 更新 tutors 表
        Tutor tutor = tutorMapper.selectOne(new QueryWrapper<Tutor>().eq("user_id", user.getId()));
        if (tutor == null) {
            tutor = new Tutor();
            tutor.setUserId(user.getId());
        }
        tutor.setBio(dto.getBio());
        tutor.setExpertise(dto.getExpertise());
        tutor.setHourlyRate(dto.getHourlyRate());
        tutor.setYearsOfExperience(dto.getYearsOfExperience());
        tutor.setCertifications(dto.getCertifications());
        tutor.setTeachingModes(dto.getTeachingModes());
        tutor.setTags(dto.getTags());

        if (tutor.getId() == null) {
            tutorMapper.insert(tutor);
        } else {
            tutorMapper.updateById(tutor);
        }
    }


//
//    @Operation(summary = "获取我的个人资料（Student）", security = { @SecurityRequirement(name = "bearerAuth") })
//    @GetMapping("/profile/student")
//    public StudentProfileUpdateDTO getMyStudentProfile() {
//        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
//        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
//
//        if (!"student".equals(user.getRole())) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not a student");
//        }
//
//        Student student = studentMapper.selectOne(new QueryWrapper<Student>().eq("user_id", user.getId()));
//
//        StudentProfileUpdateDTO dto = new StudentProfileUpdateDTO();
//        dto.setEducationLevel(student.getEducationLevel());
//        dto.setSubjectsNeeded(student.getSubjectsNeeded());
//        dto.setDescription(student.getDescription());
//
//        dto.setName(user.getName());
//        dto.setEmail(user.getEmail());
//        dto.setAddress(user.getAddress());
//
//        return dto;
//    }
//



    @Operation(
            summary = "修改我的个人资料",
            description = """
                  修改当前登录用户的个人资料，需在请求头中带上 Authorization Token。
                  支持修改地址、家教专属字段、学生专属字段，系统会根据角色自动匹配。
                  """,
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @PutMapping("/profile")
    public Map<String, Object> updateMyProfile(@RequestBody ProfileUpdateDTO payload) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        // 更新公共字段
        if (payload.getAddress() != null) {
            user.setAddress(payload.getAddress());
            Map<String, Double> coords = geocodeAddress(payload.getAddress());
            user.setLat(coords.get("lat"));
            user.setLng(coords.get("lng"));
            userMapper.updateById(user);
        }

        // 更新专属字段
        if ("tutor".equals(user.getRole()) && payload.getTutorProfile() != null) {
            TutorProfileUpdateDTO t = payload.getTutorProfile();
            Tutor tutor = tutorMapper.selectOne(new QueryWrapper<Tutor>().eq("user_id", user.getId()));
            if (tutor == null) {
                tutor = new Tutor();
                tutor.setUserId(user.getId());
                BeanUtils.copyProperties(t, tutor);
                tutorMapper.insert(tutor);
            } else {
                BeanUtils.copyProperties(t, tutor);
                tutorMapper.updateById(tutor);
            }
        } else if ("student".equals(user.getRole()) && payload.getStudentProfile() != null) {
            StudentProfileUpdateDTO s = payload.getStudentProfile();
            Student student = studentMapper.selectOne(new QueryWrapper<Student>().eq("user_id", user.getId()));
            if (student == null) {
                student = new Student();
                student.setUserId(user.getId());
                BeanUtils.copyProperties(s, student);
                studentMapper.insert(student);
            } else {
                BeanUtils.copyProperties(s, student);
                studentMapper.updateById(student);
            }
        }

        return Map.of("message", "Profile updated successfully");
    }




    /** 地址转坐标逻辑（你可以替换真实实现） */
    private Map<String, Double> geocodeAddress(String address) {
        try {
            String url = "https://nominatim.openstreetmap.org/search?q=" +
                    URLEncoder.encode(address, StandardCharsets.UTF_8) +
                    "&format=json&limit=1";

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "tutorXpert-backend");  // 必填

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Geocoding API request failed");
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String response = reader.lines().collect(Collectors.joining());
                JSONArray results = new JSONArray(response);
                if (results.isEmpty()) {
                    throw new RuntimeException("No results found");
                }
                JSONObject result = results.getJSONObject(0);
                double lat = result.getDouble("lat");
                double lon = result.getDouble("lon");
                Map<String, Double> coords = new HashMap<>();
                coords.put("lat", lat);
                coords.put("lng", lon);
                return coords;
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException("Geocoding failed", e);
        }
    }

}