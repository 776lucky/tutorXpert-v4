package com.tutorXpert.tutorxpert_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tutorXpert.tutorxpert_backend.domain.po.TaskApplication;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskApplicationMapper extends BaseMapper<TaskApplication> {
}

