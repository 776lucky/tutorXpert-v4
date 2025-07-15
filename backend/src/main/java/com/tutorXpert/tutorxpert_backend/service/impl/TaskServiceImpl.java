package com.tutorXpert.tutorxpert_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.task.*;
import com.tutorXpert.tutorxpert_backend.domain.enums.TaskStatus;
import com.tutorXpert.tutorxpert_backend.domain.po.Student;
import com.tutorXpert.tutorxpert_backend.domain.po.Task;
import com.tutorXpert.tutorxpert_backend.domain.po.TaskApplication;
import com.tutorXpert.tutorxpert_backend.domain.po.User;
import com.tutorXpert.tutorxpert_backend.mapper.StudentMapper;
import com.tutorXpert.tutorxpert_backend.mapper.TaskApplicationMapper;
import com.tutorXpert.tutorxpert_backend.mapper.TaskMapper;
import com.tutorXpert.tutorxpert_backend.mapper.UserMapper;
import com.tutorXpert.tutorxpert_backend.security.JwtUtil;
import com.tutorXpert.tutorxpert_backend.service.ITaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
public class TaskServiceImpl implements ITaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskApplicationMapper taskApplicationMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private JwtUtil jwtUtil;


    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        String email = authentication.getName();  // 这里仍是邮箱
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user.getId();
    }


    /** 发布任务 */
    @Override
    @Transactional
    public TaskDTO createTask(TaskCreateDTO dto, Long studentId) {
        Task task = new Task();
        BeanUtils.copyProperties(dto, task);
        task.setUserId(studentId);
        task.setStatus(TaskStatus.OPEN.name());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(task.getCreatedAt());
        taskMapper.insert(task);
        return toDTO(task);
    }


    @Override
    @Transactional
    public TaskDTO acceptApplication(Long studentId, Long taskId, Long applicationId) {
        // 1 任务校验
        Task task = taskMapper.selectById(taskId);
        if (task == null || !task.getUserId().equals(studentId) ||
                !TaskStatus.OPEN.name().equals(task.getStatus()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Illegal operation");

        // 2 更新申请状态
        TaskApplication app = taskApplicationMapper.selectById(applicationId);
        if (app == null || !app.getTaskId().equals(taskId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found");
        app.setStatus("Accepted");
        app.setUpdatedAt(LocalDateTime.now());
        taskApplicationMapper.updateById(app);

        // 同时拒绝其他申请
        taskApplicationMapper.rejectOthers(taskId, applicationId);

        // 3 乐观更新任务
        task.setStatus(TaskStatus.ASSIGNED.name());
        task.setAcceptedTutorId(app.getTutorId());
        task.setUpdatedAt(LocalDateTime.now());
        taskMapper.updateById(task);
        return toDTO(task);
    }


    @Override
    @Transactional
    public TaskDTO startTask(Long tutorId, Long taskId) {
        Task task = taskMapper.selectById(taskId);
        if (task == null || !task.getAcceptedTutorId().equals(tutorId) ||
                !TaskStatus.ASSIGNED.name().equals(task.getStatus()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Illegal operation");

        task.setStatus(TaskStatus.IN_PROGRESS.name());
        task.setUpdatedAt(LocalDateTime.now());
        taskMapper.updateById(task);
        return toDTO(task);
    }

    @Override
    @Transactional
    public TaskDTO completeTask(Long studentId, Long taskId) {
        Task task = taskMapper.selectById(taskId);
        if (task == null || !task.getUserId().equals(studentId) ||
                !TaskStatus.IN_PROGRESS.name().equals(task.getStatus()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Illegal operation");
        task.setStatus(TaskStatus.COMPLETED.name());
        task.setUpdatedAt(LocalDateTime.now());
        taskMapper.updateById(task);
        return toDTO(task);
    }

    @Override
    @Transactional
    public TaskDTO cancelTask(Long userId, Long taskId) {
        Task task = taskMapper.selectById(taskId);
        boolean allowed = switch (TaskStatus.valueOf(task.getStatus())) {
            case OPEN -> task.getUserId().equals(userId);
            case ASSIGNED, IN_PROGRESS ->
                    task.getUserId().equals(userId) || task.getAcceptedTutorId().equals(userId);
            default -> false;
        };
        if (!allowed) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Illegal operation");
        task.setStatus(TaskStatus.CANCELLED.name());
        task.setUpdatedAt(LocalDateTime.now());
        taskMapper.updateById(task);
        return toDTO(task);
    }


    /* private 工具 */
    private TaskDTO toDTO(Task task){
        TaskDTO dto = new TaskDTO();
        BeanUtils.copyProperties(task, dto);
        dto.setStatusLabel(TaskStatus.valueOf(task.getStatus()).name()); // 若已加 label
        return dto;
    }



    /** 获取我发布的任务 */
    @Override
    public List<TaskDTO> getMyTasks(Long userId) {
        List<Task> tasks = taskMapper.selectList(
                new QueryWrapper<Task>().eq("user_id", userId)
        );
        return tasks.stream().map(task -> {
            TaskDTO dto = new TaskDTO();
            BeanUtils.copyProperties(task, dto);
            return dto;
        }).toList();
    }



    /** 删除任务 */
    @Override
    @Transactional
    public void deleteTaskById(Long id) {
        Task task = taskMapper.selectById(id);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }

        // 1. 删除关联的申请记录
        taskApplicationMapper.delete(
                new QueryWrapper<TaskApplication>().eq("task_id", id)
        );

        // 2. 删除任务
        taskMapper.deleteById(id);
    }


    /** 获取任务详情 */
    @Override
    public TaskDTO getTaskById(Long taskId) {
        Task task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }

        TaskDTO dto = new TaskDTO();
        BeanUtils.copyProperties(task, dto);

        return dto;
    }





    /** 修改任务状态 */
    @Override
    @Transactional
    public TaskDTO updateTaskStatus(Long taskId, TaskStatusUpdateDTO dto) {
        // 1. 查询任务
        Task task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new RuntimeException("Task not found");
        }

        // 2. 更新状态
        task.setStatus(dto.getStatus());
        task.setUpdatedAt(LocalDateTime.now());

        // 3. 更新数据库
        taskMapper.updateById(task);

        // 4. 转换并返回 DTO
        TaskDTO result = new TaskDTO();
        BeanUtils.copyProperties(task, result);
        return result;
    }


    /** 地图筛选任务 */
    @Override
    public List<TaskSearchDTO> searchTasksByLocation(double minLat, double maxLat, double minLng, double maxLng) {
        List<Task> tasks = taskMapper.selectTasksWithinBounds(minLat, maxLat, minLng, maxLng);
        return tasks.stream().map(task -> {
            TaskSearchDTO dto = new TaskSearchDTO();
            BeanUtils.copyProperties(task, dto);
            return dto;
        }).toList();
    }

    /** 获取任务申请列表（待实现） */
    @Override
    public List<TaskApplicationDTO> getApplicationsByTaskId(Long taskId) {
        List<TaskApplication> applications = taskApplicationMapper.selectList(
                new QueryWrapper<TaskApplication>().eq("task_id", taskId)
        );

        List<TaskApplicationDTO> result = new ArrayList<>();
        for (TaskApplication application : applications) {
            TaskApplicationDTO dto = new TaskApplicationDTO();
            BeanUtils.copyProperties(application, dto);
            result.add(dto);
        }

        return result;
    }


    /** 审核任务申请 */
    @Override
    @Transactional
    public TaskApplicationDTO reviewApplication(Long taskId, Long applicationId, TaskApplicationDecisionDTO decision) {
        // 1. 查询申请，双重校验防止越权
        TaskApplication application = taskApplicationMapper.selectOne(
                new QueryWrapper<TaskApplication>()
                        .eq("id", applicationId)
                        .eq("task_id", taskId)
        );
        if (application == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found");
        }

        // 2. 更新状态
        String status = decision.getStatus();
        if (!"Accepted".equals(status) && !"Rejected".equals(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status value");
        }

        application.setStatus(status);
        application.setUpdatedAt(LocalDateTime.now());
        taskApplicationMapper.updateById(application);

        // 3. 转换 DTO
        TaskApplicationDTO dto = new TaskApplicationDTO();
        BeanUtils.copyProperties(application, dto);
        return dto;
    }



    /** 家教提交任务申请 */
    @Override
    @Transactional
    public TaskApplicationDTO applyForTask(Long taskId, TaskApplicationRequestDTO dto) {
        TaskApplication newApp = new TaskApplication();
        newApp.setTaskId(taskId);
        newApp.setTutorId(dto.getTutorId());
        newApp.setBidAmount(dto.getBidAmount());
        newApp.setMessage(dto.getMessage());
        newApp.setStatus("Pending");
        newApp.setCreatedAt(LocalDateTime.now());
        newApp.setUpdatedAt(LocalDateTime.now());

        try {
            taskApplicationMapper.insert(newApp);
            TaskApplicationDTO result = new TaskApplicationDTO();
            BeanUtils.copyProperties(newApp, result);
            return result;
        } catch (DuplicateKeyException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You have already applied for this task.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.");
        }
    }





    @Override
    public List<Task> getAllTasks() {
        return taskMapper.selectList(null);
    }

}

