package com.tutorXpert.tutorxpert_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.task.TaskApplicationRequest;
import com.tutorXpert.tutorxpert_backend.domain.po.Task;
import com.tutorXpert.tutorxpert_backend.domain.po.TaskApplication;
import com.tutorXpert.tutorxpert_backend.mapper.TaskApplicationMapper;
import com.tutorXpert.tutorxpert_backend.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {


    @Autowired
    private TaskApplicationMapper taskApplicationMapper;


    @Autowired
    private ITaskService taskService;

    /** 发布任务 */
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    /** 获取我发布的任务 */
    @GetMapping("/my_tasks")
    public List<Task> getMyTasks() {
        return taskService.getMyTasks();  // 你需要实现这个接口
    }

    /** 删除任务 */
    @DeleteMapping("/{task_id:\\d+}")
    public void deleteTask(@PathVariable("task_id") Long taskId) {
        taskService.deleteTaskById(taskId);
    }

    /** 获取任务详情 */
    @GetMapping("/{task_id:\\d+}")
    public Task getTaskById(@PathVariable("task_id") Long taskId) {
        return taskService.getTaskById(taskId);
    }

    /** 获取任务申请列表 */
    @GetMapping("/{task_id:\\d+}/applications")
    public List<Object> getApplications(@PathVariable("task_id") Long taskId) {
        return taskService.getApplicationsByTaskId(taskId);
    }

    /** 审核任务申请（接/拒） */
    @PostMapping("/{task_id:\\d+}/applications/{application_id:\\d+}/decision")
    public Object reviewApplication(
            @PathVariable("task_id") Long taskId,
            @PathVariable("application_id") Long applicationId,
            @RequestBody Object decisionPayload) {
        return taskService.reviewApplication(taskId, applicationId, decisionPayload);
    }

    /** 修改任务状态 */
    @PatchMapping("/{task_id:\\d+}/status")
    public Task updateTaskStatus(@PathVariable("task_id") Long taskId, @RequestBody Task task) {
        return taskService.updateTaskStatus(taskId, task);
    }

    /** 地图筛选任务 */
    @GetMapping("/search")
    public List<Task> searchTasksByLocation(
            @RequestParam double minLat,
            @RequestParam double maxLat,
            @RequestParam double minLng,
            @RequestParam double maxLng
    ) {
        return taskService.searchTasksByLocation(minLat, maxLat, minLng, maxLng);
    }


    @GetMapping("/my_applications")
    public List<Map<String, Object>> getMyApplications(@RequestParam("tutor_id") Long tutorId) {
        return new ArrayList<>();  // 假数据，防止报错
    }

    // 家教提交任务申请
// 家教提交任务申请
    @PostMapping("/{taskId}/applications")
    public ResponseEntity<?> applyForTask(
            @PathVariable Long taskId,
            @RequestBody TaskApplicationRequest request) {

        // 防止重复申请
        TaskApplication existing = taskApplicationMapper.selectOne(
                new QueryWrapper<TaskApplication>()
                        .eq("task_id", taskId)
                        .eq("tutor_id", request.getTutorId())
        );
        if (existing != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("message", "You have already applied for this task.")
            );
        }

        // 创建新申请
        TaskApplication application = new TaskApplication();
        application.setTaskId(taskId);
        application.setTutorId(request.getTutorId());
        application.setBidAmount(request.getBidAmount());
        application.setMessage(request.getMessage());
        application.setStatus("pending");
        // 如果你的表有 appliedAt 字段，可以自动填充：
        application.setAppliedAt(LocalDateTime.now());  // 确认你的实体类字段

        // 插入数据库
        taskApplicationMapper.insert(application);

        // 返回成功响应
        return ResponseEntity.ok(Map.of(
                "message", "Application submitted successfully",
                "application", application
        ));
    }


}
