package com.tutorXpert.tutorxpert_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.tutor.TutorProfileSearchPageDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT " +
            "u.id AS id, " +
            "u.email AS email, " +
            "u.role AS role, " +
            "u.lat AS lat, " +
            "u.lng AS lng, " +
            "u.address AS address, " +
            "t.bio AS bio, " +
            "t.expertise AS expertise, " +
            "t.hourly_rate AS hourly_rate " +
            "FROM users u JOIN tutors t ON u.id = t.user_id " +
            "WHERE u.lat BETWEEN LEAST(#{south}, #{north}) AND GREATEST(#{south}, #{north}) " +
            "AND u.lng BETWEEN LEAST(#{west}, #{east}) AND GREATEST(#{west}, #{east}) " +
            "AND u.role = 'tutor'")
    List<TutorProfileSearchPageDTO> searchTutorProfiles(@Param("north") double north,
                                                        @Param("south") double south,
                                                        @Param("east") double east,
                                                        @Param("west") double west);


}
