package com.silverhealth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@TableName("health_daily_report")
public class HealthDailyReport {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private LocalDate reportDate;
    private Integer activityMinutes;
    private Integer sedentaryMinutes;
    private Integer outdoorCount;
    private LocalTime sleepTime;
    private LocalTime wakeTime;
    private Integer nightWakeCount;
    private Integer sleepScore;
    /** 1平稳 2轻微低落 3消极倾向 */
    private Integer emotionStatus;
    private String emotionAdvice;
    private Integer overallScore;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
