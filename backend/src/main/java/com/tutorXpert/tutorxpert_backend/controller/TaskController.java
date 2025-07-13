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


    @Operation(summary = "Create a new task", description = "Students can create a new task")
    @PostMapping
    public TaskDTO createTask(@RequestBody @Valid TaskCreateDTO dto,
                              @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // remove "Bearer "
        Long userId = jwtUtil.getUserIdFromToken(token).longValue();
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
    public List<TaskDTO> getMyTasks(@RequestHeader("Authorization") String token) {
        Long userId = Long.valueOf(jwtUtil.getUserIdFromToken(token.substring(7)));
        return taskService.getMyTasks(userId);
    }




    @Operation(
            summary = "Delete a task",
            description = "Deletes a task by its ID. "
                    + "Only the task owner (student who created the task) can delete it. "
                    + "Ensure the task ID exists and belongs to the authenticated user."
    )
    @DeleteMapping("/{task_id}")
    public void deleteTask(@PathVariable("task_id") Long taskId) {
        taskService.deleteTaskById(taskId);
    }




    @Operation(
            summary = "Get task details by ID",
            description = "Retrieves detailed information about a specific task by its ID. "
                    + "Useful for task detail pages or reviewing task information before applying."
    )
    @GetMapping("/{task_id}")
    public TaskDTO getTaskById(@PathVariable("task_id") Long taskId,
                               @RequestHeader(value = "Authorization", required = false) String authHeader) {
        System.out.println("🧪 传入 token: " + authHeader);
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





    @Operation(
            summary = "Search tasks by map location",
            description = "Filters tasks based on map boundaries using latitude and longitude ranges. "
                    + "Useful for displaying tasks on map-based search pages. "
                    + "Returns tasks within the specified geographic region."
    )
    @GetMapping("/search")
    public List<TaskSearchDTO> searchTasksByLocation(
            @RequestParam double minLat,
            @RequestParam double maxLat,
            @RequestParam double minLng,
            @RequestParam double maxLng
    ) {
        return taskService.searchTasksByLocation(minLat, maxLat, minLng, maxLng);
    }




    @Operation(
            summary = "Get task applications",
            description = "Retrieves the list of all applications submitted for a specific task. "
                    + "Primarily used by students to review tutors who have applied for their tasks."
    )
    @GetMapping("/{task_id}/applications")
    public List<TaskApplicationDTO> getApplications(@PathVariable("task_id") Long taskId) {
        return taskService.getApplicationsByTaskId(taskId);
    }




    @Operation(
            summary = "Review task application",
            description = "Allows a student to accept or reject a specific task application submitted by a tutor. "
                    + "After accepting an application, the task status or related records may change."
    )
    @PostMapping("/{task_id}/applications/{application_id}/decision")
    public TaskApplicationDTO reviewApplication(@PathVariable("task_id") Long taskId,
                                                @PathVariable("application_id") Long applicationId,
                                                @RequestBody TaskApplicationDecisionDTO decision) {
        return taskService.reviewApplication(taskId, applicationId, decision);
    }




    @Operation(
            summary = "Apply for a task",
            description = "Enables tutors to apply for a specific task by submitting a bid amount and message. "
                    + "Duplicate applications by the same tutor for the same task are not allowed. "
                    + "Typically used by tutors on task detail pages."
    )
    @PostMapping("/{task_id}/applications")
// Controller 保持现有逻辑，不再查重，减少多余查询
    public ResponseEntity<?> applyForTask(@PathVariable("task_id") Long taskId,
                                          @RequestBody TaskApplicationRequestDTO dto,
                                          HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization").substring(7);
        String email = jwtUtil.validateToken(token);
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        Long tutorId = user.getId();

        // 强制设置 tutorId，防止伪造
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



}
