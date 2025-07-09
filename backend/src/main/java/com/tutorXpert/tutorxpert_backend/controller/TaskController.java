package com.tutorXpert.tutorxpert_backend.controller;

import com.tutorXpert.tutorxpert_backend.domain.dto.task.*;
import com.tutorXpert.tutorxpert_backend.domain.po.TaskApplication;
import com.tutorXpert.tutorxpert_backend.mapper.TaskApplicationMapper;
import com.tutorXpert.tutorxpert_backend.service.ITaskService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskService taskService;

    @Autowired
    private TaskApplicationMapper taskApplicationMapper;

    @Operation(summary = "发布新任务", description = "发布一个新任务")
    @PostMapping
    public TaskDTO createTask(@RequestBody TaskCreateDTO dto) {
        return taskService.createTask(dto);
    }

    @Operation(summary = "获取我发布的任务", description = "获取当前用户发布的所有任务")
    @GetMapping("/my_tasks")
    public List<TaskDTO> getMyTasks() {
        return taskService.getMyTasks();
    }

    @Operation(summary = "删除任务", description = "根据任务 ID 删除任务")
    @DeleteMapping("/{task_id}")
    public void deleteTask(@PathVariable("task_id") Long taskId) {
        taskService.deleteTaskById(taskId);
    }

    @Operation(summary = "获取任务详情", description = "根据任务 ID 获取任务详情")
    @GetMapping("/{task_id}")
    public TaskDTO getTaskById(@PathVariable("task_id") Long taskId) {
        return taskService.getTaskById(taskId);
    }

    @Operation(summary = "修改任务状态", description = "修改任务状态，如 Open、Closed")
    @PatchMapping("/{task_id}/status")
    public TaskDTO updateTaskStatus(@PathVariable("task_id") Long taskId,
                                    @RequestBody TaskStatusUpdateDTO dto) {
        return taskService.updateTaskStatus(taskId, dto);
    }

    @Operation(summary = "地图筛选任务", description = "根据地图边界经纬度筛选任务")
    @GetMapping("/search")
    public List<TaskSearchDTO> searchTasksByLocation(
            @RequestParam double minLat,
            @RequestParam double maxLat,
            @RequestParam double minLng,
            @RequestParam double maxLng
    ) {
        return taskService.searchTasksByLocation(minLat, maxLat, minLng, maxLng);
    }

    @Operation(summary = "获取任务申请列表", description = "获取指定任务下的所有申请")
    @GetMapping("/{task_id}/applications")
    public List<TaskApplicationDTO> getApplications(@PathVariable("task_id") Long taskId) {
        return taskService.getApplicationsByTaskId(taskId);
    }

    @Operation(summary = "审核任务申请", description = "接受或拒绝任务申请")
    @PostMapping("/{task_id}/applications/{application_id}/decision")
    public TaskApplicationDTO reviewApplication(@PathVariable("task_id") Long taskId,
                                                @PathVariable("application_id") Long applicationId,
                                                @RequestBody TaskApplicationDecisionDTO decision) {
        return taskService.reviewApplication(taskId, applicationId, decision);
    }

    @Operation(summary = "家教申请任务", description = "家教提交对任务的申请，重复申请会被拒绝")
    @PostMapping("/{task_id}/applications")
    public ResponseEntity<?> applyForTask(@PathVariable("task_id") Long taskId,
                                          @RequestBody TaskApplicationRequestDTO request) {
        TaskApplication existing = taskApplicationMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<TaskApplication>()
                        .eq("task_id", taskId)
                        .eq("tutor_id", request.getTutorId())
        );
        if (existing != null) {
            return ResponseEntity.status(409).body(Map.of("message", "You have already applied for this task."));
        }
        return taskService.applyForTask(taskId, request);
    }

    @Operation(summary = "获取我申请的任务", description = "获取当前家教申请过的所有任务")
    @GetMapping("/my_applications")
    public List<Map<String, Object>> getMyApplications(@RequestParam("tutor_id") Long tutorId) {
        return taskService.getMyApplications(tutorId);
    }
}
