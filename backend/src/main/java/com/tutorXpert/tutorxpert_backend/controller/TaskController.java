package com.tutorXpert.tutorxpert_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.task.*;
import com.tutorXpert.tutorxpert_backend.domain.po.Task;
import com.tutorXpert.tutorxpert_backend.domain.po.TaskApplication;
import com.tutorXpert.tutorxpert_backend.domain.po.User;
import com.tutorXpert.tutorxpert_backend.mapper.TaskApplicationMapper;
import com.tutorXpert.tutorxpert_backend.mapper.TaskMapper;
import com.tutorXpert.tutorxpert_backend.mapper.UserMapper;
import com.tutorXpert.tutorxpert_backend.security.JwtUtil;
import com.tutorXpert.tutorxpert_backend.service.ITaskService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskService taskService;

    @Autowired
    private TaskApplicationMapper taskApplicationMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskMapper taskMapper;


    @Autowired
    public TaskController(ITaskService taskService, JwtUtil jwtUtil) {
        this.taskService = taskService;
        this.jwtUtil = jwtUtil;
    }


    @Operation(summary = "Create a new task")
    @PostMapping
    public TaskDTO createTask(@RequestBody @Valid TaskCreateDTO dto,
                              @RequestHeader("Authorization") String auth) {
        Long userId = jwtUtil.getUserIdFromToken(auth.substring(7)).longValue();
        return taskService.createTask(dto, userId);
    }


    @Operation(
            summary = "Retrieve all tasks",
            description = "Fetches the complete list of all tasks available on the platform. "
                    + "This endpoint is typically used to display all tasks to users without filters. "
                    + "Note: For location-based filtering, use the /tasks/search endpoint."
    )
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        List<TaskDTO> dtos = new ArrayList<>();
        for (Task task : tasks) {
            TaskDTO dto = new TaskDTO();
            BeanUtils.copyProperties(task, dto);
            dtos.add(dto);
        }
        return ResponseEntity.ok(dtos);
    }






    @Operation(
            summary = "Retrieve my posted tasks",
            description = "Fetches tasks posted by the currently logged-in student. "
                    + "Authentication is required. The system identifies the user from the JWT token."
    )
    @GetMapping("/my_tasks")
    public List<TaskDTO> getMyTasks(@RequestHeader("Authorization") String auth) {
        Long userId = jwtUtil.getUserIdFromToken(auth.substring(7)).longValue();
        return taskService.getMyTasks(userId);
    }




    @Operation(summary = "Delete a task")
    @DeleteMapping("/{task_id}")
    public void deleteTask(@PathVariable("task_id") Long taskId) {
        taskService.deleteTaskById(taskId);
    }



    @Operation(summary = "Get task details")
    @GetMapping("/{task_id}")
    public TaskDTO getTaskById(@PathVariable("task_id") Long taskId) {
        return taskService.getTaskById(taskId);
    }




    @Operation(
            summary = "Update task status",
            description = "Updates the status of a specific task (e.g., Open, Closed). "
                    + "Only the task owner (student) can perform this action. "
                    + "Typically used to close tasks after they have been fulfilled."
    )
    @PatchMapping("/{task_id}/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(@PathVariable("task_id") Long taskId,
                                                    @RequestBody TaskStatusUpdateDTO dto,
                                                    HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtUtil.validateToken(token);
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));

        Task task = taskMapper.selectById(taskId);
        if (task == null || !task.getUserId().equals(user.getId())) {
            throw new RuntimeException("No permission to update this task");
        }

        TaskDTO updatedTask = taskService.updateTaskStatus(taskId, dto);
        return ResponseEntity.ok(updatedTask);
    }





    @Operation(summary = "Search tasks by location")
    @GetMapping("/search")
    public List<TaskSearchDTO> searchTasksByLocation(
            @RequestParam double minLat,
            @RequestParam double maxLat,
            @RequestParam double minLng,
            @RequestParam double maxLng) {
        return taskService.searchTasksByLocation(minLat, maxLat, minLng, maxLng);
    }




    @Operation(summary = "Get applications for a task")
    @GetMapping("/{task_id}/applications")
    public List<TaskApplicationDTO> getApplications(@PathVariable("task_id") Long taskId) {
        return taskService.getApplicationsByTaskId(taskId);
    }





    @Operation(summary = "Review (accept/reject) a task application")
    @PostMapping("/{task_id}/applications/{application_id}/decision")
    public TaskApplicationDTO reviewApplication(@PathVariable("task_id") Long taskId,
                                                @PathVariable("application_id") Long appId,
                                                @RequestBody TaskApplicationDecisionDTO decision) {
        return taskService.reviewApplication(taskId, appId, decision);
    }




    @Operation(summary = "Tutor applies for a task")
    @PostMapping("/{task_id}/applications")
    public TaskApplicationDTO applyForTask(@PathVariable("task_id") Long taskId,
                                           @RequestBody @Valid TaskApplicationRequestDTO dto,
                                           @RequestHeader("Authorization") String auth) {
        Long tutorId = jwtUtil.getUserIdFromToken(auth.substring(7)).longValue();
        dto.setTutorId(tutorId);
        return taskService.applyForTask(taskId, dto);
    }




    @Operation(
            summary = "Edit a task",
            description = "Allows students to edit an existing task they have posted. "
                    + "Editable fields include title, subject, description, address, location coordinates (lat, lng), budget, and deadline. "
                    + "Typically used by students on the task management page to update task details before the task is accepted."
    )
    @PutMapping("/{taskId}")
    public String updateTask(@PathVariable("taskId") Long taskId,
                             @RequestBody TaskUpdateDTO dto) {
        Task task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }

        BeanUtils.copyProperties(dto, task);
        task.setUpdatedAt(LocalDateTime.now());

        taskMapper.updateById(task);
        return "Task updated successfully";
    }



    @Operation(summary = "Tutor starts the task (IN_PROGRESS)")
    @PatchMapping("/{task_id}/start")
    public TaskDTO startTask(@PathVariable("task_id") Long taskId,
                             @RequestHeader("Authorization") String auth) {
        Long tutorId = jwtUtil.getUserIdFromToken(auth.substring(7)).longValue();
        return taskService.startTask(tutorId, taskId);
    }

    @Operation(summary = "Student completes the task (COMPLETED)")
    @PatchMapping("/{task_id}/complete")
    public TaskDTO completeTask(@PathVariable("task_id") Long taskId,
                                @RequestHeader("Authorization") String auth) {
        Long studentId = jwtUtil.getUserIdFromToken(auth.substring(7)).longValue();
        return taskService.completeTask(studentId, taskId);
    }

    @Operation(summary = "Cancel the task (CANCELLED)")
    @PatchMapping("/{task_id}/cancel")
    public TaskDTO cancelTask(@PathVariable("task_id") Long taskId,
                              @RequestHeader("Authorization") String auth) {
        Long userId = jwtUtil.getUserIdFromToken(auth.substring(7)).longValue();
        return taskService.cancelTask(userId, taskId);
    }
}
