package com.tutorXpert.tutorxpert_backend.service;

import com.tutorXpert.tutorxpert_backend.domain.dto.task.*;
import com.tutorXpert.tutorxpert_backend.domain.po.Task;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ITaskService {



    /** 获取我发布的任务 */
    List<TaskDTO> getMyTasks(Long userId);

    /** 删除任务 */
    void deleteTaskById(Long id);

    /** 获取任务详情 */
    TaskDTO getTaskById(Long id);

    /** 修改任务状态 */
    TaskDTO updateTaskStatus(Long taskId, TaskStatusUpdateDTO task);

    /** 地图筛选任务 */
    List<TaskSearchDTO> searchTasksByLocation(double minLat, double maxLat, double minLng, double maxLng);

    /** 获取任务申请列表 */
    List<TaskApplicationDTO> getApplicationsByTaskId(Long taskId);

    /** 审核任务申请 */
    TaskApplicationDTO reviewApplication(Long taskId, Long applicationId, TaskApplicationDecisionDTO decision);

    /** 家教提交任务申请 */
    ResponseEntity<?> applyForTask(Long taskId, TaskApplicationRequestDTO request);

    /** 获取所有任务 */
    List<Task> getAllTasks();

    /**
     * 创建任务
     * @param dto 任务创建DTO
     * @return 创建的任务DTO
     */
    TaskDTO createTask(TaskCreateDTO dto, Long userId);
}