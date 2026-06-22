package com.silverhealth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("visit_record")
public class VisitRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long elderId;
    private Long workerId;
    /** 1日常巡访 2告警响应 3帮扶服务 */
    private Integer visitType;
    private LocalDateTime visitTime;
    private String content;
    private String result;
    private Long alertId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
