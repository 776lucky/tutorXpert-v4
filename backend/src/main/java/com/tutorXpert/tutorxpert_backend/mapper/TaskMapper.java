package com.tutorXpert.tutorxpert_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tutorXpert.tutorxpert_backend.domain.po.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;



@Mapper
public interface TaskMapper extends BaseMapper<Task> {

    @Select("""
    SELECT *
    FROM tasks
    WHERE lat BETWEEN #{minLat} AND #{maxLat}
      AND lng BETWEEN #{minLng} AND #{maxLng}
""")
    List<Task> selectTasksWithinBounds(
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLng") double minLng,
            @Param("maxLng") double maxLng
    );
}
