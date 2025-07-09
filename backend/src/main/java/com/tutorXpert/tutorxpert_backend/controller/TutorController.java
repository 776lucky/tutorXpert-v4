package com.tutorXpert.tutorxpert_backend.controller;

import com.tutorXpert.tutorxpert_backend.domain.dto.user.TutorProfileSearchPageDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.Tutor;
import com.tutorXpert.tutorxpert_backend.mapper.UserMapper;
import com.tutorXpert.tutorxpert_backend.service.ITutorService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tutors")
public class TutorController {

    @Autowired
    private ITutorService tutorService;

    @Autowired
    private UserMapper userMapper;

    /** 管理后台/测试：获取所有家教 */
    @Operation(summary = "获取所有家教（后台、测试用）")
    @GetMapping
    public List<Tutor> getAllTutors() {
        return tutorService.getAllTutors();
    }

    /** 管理后台/测试：新增家教 */
    @Operation(summary = "新增家教（后台、测试用）")
    @PostMapping
    public Tutor createTutor(@RequestBody Tutor tutor) {
        return tutorService.createTutor(tutor);
    }

    /** 地图搜索：前台家教地图搜索 */
    @Operation(summary = "地图家教搜索（前台用）")
    @GetMapping("/search")
    public List<TutorProfileSearchPageDTO> searchTutors(
            @RequestParam double north,
            @RequestParam double south,
            @RequestParam double east,
            @RequestParam double west) {
        return tutorService.searchTutors(north, south, east, west);
    }
}
