package com.silverhealth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("device_permission")
public class DevicePermission {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Integer cameraEnabled;
    private Integer micEnabled;
    private Integer screenOn;
    private Integer lowPowerMode;
    private Integer localNotify;
    private Integer smsNotify;
    private Integer monitorStatus;
    private Integer privacyAgreed;
    private Integer noRemind;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
