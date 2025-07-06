package com.tutorXpert.tutorxpert_backend.service;

import com.tutorXpert.tutorxpert_backend.domain.po.Task;

import java.util.List;

public interface ITaskService {

    List<Task> getAllTasks();

    Task getTaskById(Long id);

    Task createTask(Task task);

    void deleteTaskById(Long id);

    List<Task> getMyTasks();

    List<Object> getApplicationsByTaskId(Long taskId);

    Object reviewApplication(Long taskId, Long applicationId, Object decisionPayload);

    Task updateTaskStatus(Long taskId, Task task);
}
