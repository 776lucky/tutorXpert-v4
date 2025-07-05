package com.tutorXpert.tutorxpert_backend.service.impl;

import com.tutorXpert.tutorxpert_backend.domain.po.TaskApplication;
import com.tutorXpert.tutorxpert_backend.mapper.TaskApplicationMapper;
import com.tutorXpert.tutorxpert_backend.service.ITaskApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskApplicationServiceImpl implements ITaskApplicationService {

    @Autowired
    private TaskApplicationMapper taskApplicationMapper;

    @Override
    public List<TaskApplication> getAllApplications() {
        return taskApplicationMapper.selectList(null);
    }

    @Override
    public TaskApplication getApplicationById(Long id) {
        return taskApplicationMapper.selectById(id);
    }

    @Override
    public TaskApplication apply(TaskApplication application) {
        taskApplicationMapper.insert(application);
        return application;
    }

    @Override
    public void deleteApplicationById(Long id) {
        taskApplicationMapper.deleteById(id);
    }
}
