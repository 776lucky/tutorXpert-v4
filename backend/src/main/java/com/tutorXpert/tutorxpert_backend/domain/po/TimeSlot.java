// TimeSlot.java
package com.tutorXpert.tutorxpert_backend.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("time_slots")
public class TimeSlot {
    private Long id;
    private java.sql.Time startTime;
    private java.sql.Time endTime;
}