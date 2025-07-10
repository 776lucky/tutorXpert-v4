package com.tutorXpert.tutorxpert_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tutorXpert.tutorxpert_backend.domain.po.TaskApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
public interface TaskApplicationMapper extends BaseMapper<TaskApplication> {

    @Select("""
    SELECT ta.*, t.title, t.subject, t.address, t.deadline
    FROM task_applications ta
    JOIN tasks t ON ta.task_id = t.id
    WHERE ta.tutor_id = #{tutorId}
    ORDER BY ta.created_at DESC
""")
    List<Map<String, Object>> selectMyApplications(@Param("tutorId") Long tutorId);


}

