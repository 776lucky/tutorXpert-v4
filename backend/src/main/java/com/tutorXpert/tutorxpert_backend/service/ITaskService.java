package com.tutorXpert.tutorxpert_backend.service;

import com.tutorXpert.tutorxpert_backend.domain.dto.task.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ITaskService {

    /** 发布任务 */
    TaskDTO createTask(TaskCreateDTO task);

    /** 获取我发布的任务 */
    List<TaskDTO> getMyTasks();

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
    TaskApplicationDTO reviewApplication(Long taskId, Long applicationId, TaskApplicationDecisionDTO decisionPayload);

    /** 家教提交任务申请 */
    ResponseEntity<?> applyForTask(Long taskId, TaskApplicationRequestDTO request);

    /** 获取我申请的任务 */
    List<Map<String, Object>> getMyApplications(Long tutorId);
}
