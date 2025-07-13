package com.tutorXpert.tutorxpert_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.task.TaskApplicationDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.TaskApplication;
import com.tutorXpert.tutorxpert_backend.domain.po.User;
import com.tutorXpert.tutorxpert_backend.mapper.UserMapper;
import com.tutorXpert.tutorxpert_backend.security.JwtUtil;
import com.tutorXpert.tutorxpert_backend.service.ITaskApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class TaskApplicationController {

    @Autowired
    private ITaskApplicationService taskApplicationService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;


    /**
     * 获取所有任务申请记录
     * 用途：管理员后台管理所有申请。
     * 输入：无
     * 输出：所有申请记录列表（List<TaskApplication>）
     */
    @Operation(
            summary = "Get all task applications (Admin only)",
            description = "Retrieves all task applications on the platform. "
                    + "This endpoint is intended for admin use only."
    )
    @GetMapping
    public List<TaskApplication> getAllApplications() {
        return taskApplicationService.getAllApplications();
    }



    /**
     * 获取指定申请详情
     * 用途：管理员后台查看具体申请详情。
     * 输入：申请 ID
     * 输出：申请详情（TaskApplication）
     */
    @Operation(
            summary = "Get task application by ID (Admin only)",
            description = "Fetches detailed information about a specific task application by its ID. "
                    + "This endpoint is intended for admin use only."
    )
    @GetMapping("/{id}")
    public TaskApplication getApplicationById(@PathVariable Long id) {
        return taskApplicationService.getApplicationById(id);
    }



    /**
     * 删除申请（取消申请）
     * 用途：家教取消自己的申请，也可供管理员删除申请。
     * 输入：申请 ID
     * 输出：无
     */
    @Operation(
            summary = "Delete task application (Cancel application)",
            description = "Deletes a specific task application by its ID. "
                    + "Tutors can use this endpoint to cancel their application."
    )
    @DeleteMapping("/{id}")
    public void deleteApplication(@PathVariable Long id) {
        taskApplicationService.deleteApplicationById(id);
    }



    /**
     * 获取当前登录家教的所有申请记录
     * 用途：家教查看自己的申请历史。
     * 输入：JWT Token（请求头中 Authorization）
     * 输出：当前 tutor 提交的申请记录列表（List<TaskApplication>）
     */
    @GetMapping("/my_applications")
    @Operation(
            summary = "Get my task applications",
            description = "Retrieves the list of all task applications submitted by the currently logged-in tutor. "
                    + "Useful for tutors to review their application history.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    public List<TaskApplicationDTO> getMyApplications(
            @RequestHeader("Authorization") String authHeader  // ← 显式声明 token 参数
    ) {
        String token = authHeader.substring(7);
        String email = jwtUtil.validateToken(token);
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        return taskApplicationService.getApplicationsByTutorId(user.getId());
    }
}
