package com.tutorXpert.tutorxpert_backend.service.impl;

import com.tutorXpert.tutorxpert_backend.domain.po.Task;
import com.tutorXpert.tutorxpert_backend.mapper.TaskMapper;
import com.tutorXpert.tutorxpert_backend.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements ITaskService {

    @Autowired
    private TaskMapper taskMapper;

    /** 获取所有任务 */
    @Override
    public List<Task> getAllTasks() {
        return taskMapper.selectList(null);
    }

    /** 获取任务详情 */
    @Override
    public Task getTaskById(Long id) {
        return taskMapper.selectById(id);
    }

    /** 发布任务 */
    @Override
    public Task createTask(Task task) {
        taskMapper.insert(task);
        return task;
    }

    /** 删除任务 */
    @Override
    public void deleteTaskById(Long id) {
        taskMapper.deleteById(id);
    }

    /** 获取我发布的任务（暂时 mock，需接入登录用户） */
    @Override
    public List<Task> getMyTasks() {
        // TODO: 接入当前用户，筛选我发布的任务
        return taskMapper.selectList(null);
    }

    /** 获取任务申请列表（暂时空实现） */
    @Override
    public List<Object> getApplicationsByTaskId(Long taskId) {
        // TODO: 查询数据库中的申请列表
        return List.of();
    }

    /** 审核任务申请（暂时空实现） */
    @Override
    public Object reviewApplication(Long taskId, Long applicationId, Object decisionPayload) {
        // TODO: 实现申请审核逻辑
        return null;
    }

    /** 修改任务状态（示例简单更新） */
    @Override
    public Task updateTaskStatus(Long taskId, Task task) {
        task.setId(taskId);
        taskMapper.updateById(task);
        return task;
    }

    @Override
    public List<Task> searchTasksByLocation(double minLat, double maxLat, double minLng, double maxLng) {
        return taskMapper.selectTasksWithinBounds(minLat, maxLat, minLng, maxLng);
    }

}
