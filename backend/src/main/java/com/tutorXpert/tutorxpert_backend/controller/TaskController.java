package com.tutorXpert.tutorxpert_backend.controller;

import com.tutorXpert.tutorxpert_backend.domain.po.Task;
import com.tutorXpert.tutorxpert_backend.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

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
}
