package com.tutorXpert.tutorxpert_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.task.*;
import com.tutorXpert.tutorxpert_backend.domain.po.Task;
import com.tutorXpert.tutorxpert_backend.domain.po.User;
import com.tutorXpert.tutorxpert_backend.mapper.TaskMapper;
import com.tutorXpert.tutorxpert_backend.mapper.UserMapper;
import com.tutorXpert.tutorxpert_backend.service.ITaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;



@Service
public class TaskServiceImpl implements ITaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserMapper userMapper;

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
    public TaskDTO createTask(TaskCreateDTO dto) {
        Task task = new Task();
        task.setUserId(getCurrentUserId());
        task.setTitle(dto.getTitle());
        task.setSubject(dto.getSubject());
        task.setDescription(dto.getDescription());
        task.setAddress(dto.getAddress());
        task.setLat(dto.getLat());
        task.setLng(dto.getLng());
        task.setBudget(dto.getBudget());
        task.setDeadline(dto.getDeadline());
        task.setStatus("Open");
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        taskMapper.insert(task);

        TaskDTO taskDTO = new TaskDTO();
        BeanUtils.copyProperties(task, taskDTO);
        return taskDTO;
    }

    /** 获取我发布的任务 */
    @Override
    public List<TaskDTO> getMyTasks() {
        List<Task> tasks = taskMapper.selectList(null);
        return tasks.stream().map(task -> {
            TaskDTO dto = new TaskDTO();
            BeanUtils.copyProperties(task, dto);
            return dto;
        }).toList();
    }

    /** 删除任务 */
    @Override
    public void deleteTaskById(Long id) {
        taskMapper.deleteById(id);
    }

    /** 获取任务详情 */
    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = taskMapper.selectById(id);
        TaskDTO dto = new TaskDTO();
        BeanUtils.copyProperties(task, dto);
        return dto;
    }

    /** 修改任务状态 */
    @Override
    public TaskDTO updateTaskStatus(Long taskId, TaskStatusUpdateDTO dto) {
        Task task = new Task();
        task.setId(taskId);
        task.setStatus(dto.getStatus());
        task.setUpdatedAt(LocalDateTime.now());
        taskMapper.updateById(task);

        Task updatedTask = taskMapper.selectById(taskId);
        TaskDTO result = new TaskDTO();
        BeanUtils.copyProperties(updatedTask, result);
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
        // TODO: 实现
        return List.of();
    }

    /** 审核任务申请（待实现） */
    @Override
    public TaskApplicationDTO reviewApplication(Long taskId, Long applicationId, TaskApplicationDecisionDTO decisionPayload) {
        // TODO: 实现
        return null;
    }

    /** 家教提交任务申请（待实现） */
    @Override
    public ResponseEntity<?> applyForTask(Long taskId, TaskApplicationRequestDTO request) {
        // TODO: 实现
        return ResponseEntity.ok().build();
    }

    /** 获取我申请的任务（待实现） */
    @Override
    public List<Map<String, Object>> getMyApplications(Long tutorId) {
        // TODO: 实现
        return List.of();
    }
}

