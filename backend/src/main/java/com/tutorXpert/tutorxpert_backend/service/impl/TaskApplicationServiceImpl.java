package com.tutorXpert.tutorxpert_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tutorXpert.tutorxpert_backend.domain.po.TaskApplication;
import com.tutorXpert.tutorxpert_backend.mapper.TaskApplicationMapper;
import com.tutorXpert.tutorxpert_backend.service.ITaskApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 任务申请（TaskApplication）服务层实现
 *
 * 职责：
 * - 专门处理任务申请的查询与删除操作
 * - 申请提交功能已由 TaskService 负责，这里不处理
 */
@Service
public class TaskApplicationServiceImpl implements ITaskApplicationService {

    @Autowired
    private TaskApplicationMapper taskApplicationMapper;

    /**
     * 获取所有任务申请记录（管理员用）
     */
    @Override
    public List<TaskApplication> getAllApplications() {
        return taskApplicationMapper.selectList(null);
    }

    /**
     * 获取指定申请记录详情（管理员用）
     */
    @Override
    public TaskApplication getApplicationById(Long id) {
        return taskApplicationMapper.selectById(id);
    }

    /**
     * 删除申请（取消申请）
     */
    @Override
    public void deleteApplicationById(Long id) {
        taskApplicationMapper.deleteById(id);
    }

    /**
     * 获取当前家教提交的申请记录
     */
    @Override
    public List<TaskApplication> getApplicationsByTutorId(Long tutorId) {
        return taskApplicationMapper.selectList(
                new QueryWrapper<TaskApplication>().eq("tutor_id", tutorId)
        );
    }
}
