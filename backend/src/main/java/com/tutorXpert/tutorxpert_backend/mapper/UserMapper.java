package com.tutorXpert.tutorxpert_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tutorXpert.tutorxpert_backend.domain.dto.TutorLocationDTO;
import com.tutorXpert.tutorxpert_backend.domain.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT id, email, lat, lng, address FROM users WHERE lat BETWEEN #{south} AND #{north} AND lng BETWEEN #{west} AND #{east} AND role = 'tutor'")
    List<TutorLocationDTO> searchTutors(double north, double south, double east, double west);
}
