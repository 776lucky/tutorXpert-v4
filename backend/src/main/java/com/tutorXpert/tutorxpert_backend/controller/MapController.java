package com.tutorXpert.tutorxpert_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.user.UserLocationDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.User;
import com.tutorXpert.tutorxpert_backend.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/map")
public class MapController {

    @Autowired
    private UserMapper userMapper;

    /**
     * 【接口名称】地图区域用户搜索接口
     * 【接口路径】GET /users/location
     * 【接口说明】根据地图边界和角色，筛选指定区域内用户，用于地图附近搜索
     * 【请求参数】
     *   - role：用户角色（tutor/student）
     *   - north, south, east, west：地图边界坐标
     * 【是否需要登录】否（公共接口）
     */


    @GetMapping("/users")
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
}
