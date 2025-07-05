package com.tutorXpert.tutorxpert_backend.service;

import com.tutorXpert.tutorxpert_backend.domain.po.TaskApplication;

import java.util.List;

public interface ITaskApplicationService {
    List<TaskApplication> getAllApplications();
    TaskApplication getApplicationById(Long id);
    TaskApplication apply(TaskApplication application);
    void deleteApplicationById(Long id);
}