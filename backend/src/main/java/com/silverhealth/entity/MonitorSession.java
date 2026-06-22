package com.silverhealth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("monitor_session")
public class MonitorSession {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private LocalDateTime sessionStart;
    private LocalDateTime sessionEnd;
    /** 1正常模式 2夜间省电模式 */
    private Integer mode;
    /** 0已结束 1进行中 */
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
