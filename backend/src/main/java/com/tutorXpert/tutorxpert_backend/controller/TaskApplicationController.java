package com.tutorXpert.tutorxpert_backend.controller;

import com.tutorXpert.tutorxpert_backend.domain.po.TaskApplication;
import com.tutorXpert.tutorxpert_backend.service.ITaskApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class TaskApplicationController {

    @Autowired
    private ITaskApplicationService taskApplicationService;

    @GetMapping
    public List<TaskApplication> getAllApplications() {
        return taskApplicationService.getAllApplications();
    }

    @GetMapping("/{id}")
    public TaskApplication getApplicationById(@PathVariable Long id) {
        return taskApplicationService.getApplicationById(id);
    }

    @PostMapping
    public TaskApplication applyForTask(@RequestBody TaskApplication application) {
        return taskApplicationService.apply(application);
    }

    @DeleteMapping("/{id}")
    public void deleteApplication(@PathVariable Long id) {
        taskApplicationService.deleteApplicationById(id);
    }

    @GetMapping("/my_applications")
    public List<TaskApplication> getMyApplications(@RequestParam Long tutorId) {
        return taskApplicationService.getApplicationsByTutorId(tutorId);
    }

}
