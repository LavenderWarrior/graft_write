package com.silverhealth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("guardian_bindng")
public class GuardianBinding {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long elderId;
    private Long guardianId;
    /** 1子女 2社区管理员 */
    private Integer guardianType;
    /** 1仅查看 2接收告警 3全部权限 */
    private Integer permission;
    private String bindCode;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
