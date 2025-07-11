package com.tutorXpert.tutorxpert_backend.controller;

import com.tutorXpert.tutorxpert_backend.domain.dto.tutor.TutorDetailDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.tutor.TutorMapSearchResultDTO;
import com.tutorXpert.tutorxpert_backend.domain.dto.tutor.TutorProfileSearchPageDTO;
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

    /** 管理后台/测试：新增家教
     * 结论：这个接口的作用是直接往 tutors 表插入家教数据，属于后台测试、数据初始化用途，不面向前台用户。
     * */
    @Operation(summary = "新增家教（后台、测试用）")
    @PostMapping
    public Tutor createTutor(@RequestBody Tutor tutor) {
        return tutorService.createTutor(tutor);
    }

    @Operation(summary = "Search tutors by map bounds")
    @GetMapping("/search")
    public List<TutorMapSearchResultDTO> searchTutorsByMapBounds(
            @RequestParam double north,
            @RequestParam double south,
            @RequestParam double east,
            @RequestParam double west) {
        return tutorService.searchTutorsByMapBounds(north, south, east, west);
    }


    @Operation(summary = "Get tutor detail by ID (for appointment)")
    @GetMapping("/{tutor_id}")
    public TutorDetailDTO getTutorDetailById(@PathVariable("tutor_id") Long tutorId) {
        return tutorService.getTutorDetailById(tutorId);
    }

}
