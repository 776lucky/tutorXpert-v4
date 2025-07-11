package com.tutorXpert.tutorxpert_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.tutor.TutorMapSearchResultDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.Tutor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TutorMapper extends BaseMapper<Tutor> {

    @Select("""
        SELECT t.id AS tutorId, t.bio, t.expertise, t.hourly_rate AS hourlyRate, u.lat, u.lng
        FROM tutors t
        JOIN users u ON t.user_id = u.id
        WHERE u.lat BETWEEN #{south} AND #{north}
          AND u.lng BETWEEN #{west} AND #{east}
    """)
    List<TutorMapSearchResultDTO> searchTutorsByMapBounds(
            @Param("north") double north,
            @Param("south") double south,
            @Param("east") double east,
            @Param("west") double west);
}

