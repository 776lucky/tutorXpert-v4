package com.tutorXpert.tutorxpert_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tutorXpert.tutorxpert_backend.domain.po.TaskApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
public interface TaskApplicationMapper extends BaseMapper<TaskApplication> {

    @Update("""
    UPDATE task_applications
    SET status = 'Rejected', updated_at = NOW()
    WHERE task_id = #{taskId}
      AND id != #{applicationId}
      AND status = 'Pending'
    """)
    int rejectOthers(@Param("taskId") Long taskId,
                     @Param("applicationId") Long applicationId);



    @Select("""
    SELECT ta.*, t.title, t.subject, t.address, t.deadline
    FROM task_applications ta
    JOIN tasks t ON ta.task_id = t.id
    WHERE ta.tutor_id = #{tutorId}
    ORDER BY ta.created_at DESC
""")
    List<Map<String, Object>> selectMyApplications(@Param("tutorId") Long tutorId);


}

