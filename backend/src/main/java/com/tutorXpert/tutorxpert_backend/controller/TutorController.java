package com.tutorXpert.tutorxpert_backend.controller;

import com.tutorXpert.tutorxpert_backend.domain.dto.TutorProfileSearchPageDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.Tutor;
import com.tutorXpert.tutorxpert_backend.mapper.UserMapper;
import com.tutorXpert.tutorxpert_backend.service.ITutorService;
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

    @GetMapping
    public List<Tutor> getAllTutors() {
        return tutorService.getAllTutors();
    }

    @PostMapping
    public Tutor createTutor(@RequestBody Tutor tutor) {
        return tutorService.createTutor(tutor);
    }

    @GetMapping("/search")
    public List<TutorProfileSearchPageDTO> searchTutors(
            @RequestParam double north,
            @RequestParam double south,
            @RequestParam double east,
            @RequestParam double west) {
        return userMapper.searchTutorProfiles(north, south, east, west);
    }

}
