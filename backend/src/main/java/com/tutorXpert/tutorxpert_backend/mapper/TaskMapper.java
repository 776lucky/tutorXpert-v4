package com.tutorXpert.tutorxpert_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tutorXpert.tutorxpert_backend.domain.po.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {

    /** 地图筛选任务：查询指定经纬度范围内的任务 */
    @Select("""
            SELECT * FROM tasks
            WHERE lat BETWEEN LEAST(#{minLat}, #{maxLat}) AND GREATEST(#{minLat}, #{maxLat})
              AND lng BETWEEN LEAST(#{minLng}, #{maxLng}) AND GREATEST(#{minLng}, #{maxLng})
            """)
    List<Task> selectTasksWithinBounds(double minLat, double maxLat, double minLng, double maxLng);


}
