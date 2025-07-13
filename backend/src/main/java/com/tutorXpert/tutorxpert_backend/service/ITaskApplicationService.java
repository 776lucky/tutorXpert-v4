package com.tutorXpert.tutorxpert_backend.service;

import com.tutorXpert.tutorxpert_backend.domain.dto.task.TaskApplicationDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.TaskApplication;

import java.util.List;

/**
 * 任务申请（TaskApplication）服务层接口
 *
 * 职责：专门处理任务申请相关的业务逻辑。
 * 适用用户：主要面向家教（Tutor）和后台管理员。
 *
 * 功能：
 * - 家教查看自己的申请记录
 * - 家教取消自己的申请
 * - 管理员查询所有申请记录、查看申请详情、删除申请
 * （申请提交接口不放这里，已在 TaskService 中实现）
 */
public interface ITaskApplicationService {

    /**
     * 获取当前家教提交的所有申请记录
     * @param tutorId 家教用户 ID
     * @return 该家教提交的所有申请
     */
    List<TaskApplicationDTO> getApplicationsByTutorId(Long tutorId);

    /**
     * 根据申请 ID 删除申请（取消申请）
     * @param id 申请记录 ID
     */
    void deleteApplicationById(Long id);

    /**
     * 获取所有申请记录（管理员用）
     * @return 所有申请记录
     */
    List<TaskApplication> getAllApplications();

    /**
     * 根据申请 ID 获取申请详情（管理员用）
     * @param id 申请记录 ID
     * @return 申请详情
     */
    TaskApplication getApplicationById(Long id);
}
