package com.tutorXpert.tutorxpert_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tutorXpert.tutorxpert_backend.domain.po.TimeSlot;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TimeSlotMapper extends BaseMapper<TimeSlot> { }