package com.tutorXpert.tutorxpert_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tutorXpert.tutorxpert_backend.domain.po.Task;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {

    /** 地图筛选：仅返回 OPEN 状态的任务 */
    @Select("""
        SELECT *
        FROM tasks
        WHERE status = 'OPEN'
          AND lat BETWEEN LEAST(#{minLat}, #{maxLat}) AND GREATEST(#{minLat}, #{maxLat})
          AND lng BETWEEN LEAST(#{minLng}, #{maxLng}) AND GREATEST(#{minLng}, #{maxLng})
        """)
    List<Task> selectTasksWithinBounds(@Param("minLat") double minLat,
                                       @Param("maxLat") double maxLat,
                                       @Param("minLng") double minLng,
                                       @Param("maxLng") double maxLng);

    /** OPEN → ASSIGNED：指派家教并更新状态，行数 = 1 表示成功 */
    @Update("""
        UPDATE tasks
        SET status = #{newStatus},
            accepted_tutor_id = #{tutorId},
            updated_at = #{updateTime}
        WHERE id = #{taskId} AND status = #{expectedStatus}
        """)
    int updateStatusWithTutor(@Param("taskId") Long taskId,
                              @Param("expectedStatus") String expectedStatus,
                              @Param("newStatus") String newStatus,
                              @Param("tutorId") Long tutorId,
                              @Param("updateTime") LocalDateTime updateTime);

    /** 通用状态流转：ASSIGNED/IN_PROGRESS → 其他终态 */
    @Update("""
        UPDATE tasks
        SET status = #{newStatus},
            updated_at = #{updateTime}
        WHERE id = #{taskId} AND status = #{expectedStatus}
        """)
    int updateStatus(@Param("taskId") Long taskId,
                     @Param("expectedStatus") String expectedStatus,
                     @Param("newStatus") String newStatus,
                     @Param("updateTime") LocalDateTime updateTime);
}
