package com.silverhealth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("health_period_report")
public class HealthPeriodReport {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    /** 1周报 2月报 */
    private Integer reportType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer avgActivity;
    private Integer avgSleepScore;
    private String emotionTrend;
    private Integer alertCount;
    private String summary;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
