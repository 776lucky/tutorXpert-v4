package com.tutorXpert.tutorxpert_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tutorXpert.tutorxpert_backend.domain.po.Appointment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {

    // 查学生预约
    @Select("SELECT * FROM appointments WHERE student_id = #{sid} ORDER BY created_at DESC")
    List<Appointment> findByStudent(@Param("sid") Long studentId);

    // 查 Tutor 待处理预约
    @Select("SELECT * FROM appointments WHERE tutor_id = #{tid} AND status = 'Pending'")
    List<Appointment> findPendingByTutor(@Param("tid") Long tutorId);
}