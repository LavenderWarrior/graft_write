package com.silverhealth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("family_notify_setting")
public class FamilyNotifySetting {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Integer wechatNotify;
    private Integer smsNotify;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
