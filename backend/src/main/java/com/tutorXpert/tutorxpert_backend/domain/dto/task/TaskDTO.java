package com.tutorXpert.tutorxpert_backend.domain.dto.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDTO {

    @Schema(description = "任务ID")
    private Long id;

    @Schema(description = "发布人 ID")
    private Long userId;

    @Schema(description = "任务标题")
    private String title;

    @Schema(description = "科目")
    private String subject;

    @Schema(description = "任务描述")
    private String description;

    @Schema(description = "任务地址")
    private String address;

    @Schema(description = "纬度")
    private Double lat;

    @Schema(description = "经度")
    private Double lng;

    @Schema(description = "预算")
    private String budget;

    @Schema(description = "截止时间")
    private LocalDateTime deadline;

    @Schema(description = "任务状态")
    private String status;

    @Schema(description = "接受任务的家教ID", oneOf = {Long.class}, nullable = true)
    private Long acceptedTutorId;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @Schema(description = "学生姓名", oneOf = {String.class}, nullable = true)
    private String studentName;

    @Schema(description = "学生教育水平", oneOf = {String.class}, nullable = true)
    private String studentEducationLevel;
}
