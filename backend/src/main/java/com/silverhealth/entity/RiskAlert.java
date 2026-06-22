package com.silverhealth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("risk_alert")
public class RiskAlert {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    /** 1疑似跌倒 2长时间不动 3情绪低落 4夜间异响 5长时间静坐 6作息异常 */
    private Integer alertType;
    /** 1低风险 2中风险 3高风险 */
    private Integer riskLevel;
    private String description;
    private Integer notifiedFamily;
    private Integer notifiedCommunity;
    /** 0未处理 1已处理 2误报已取消 */
    private Integer handleStatus;
    private String handleResult;
    private LocalDateTime handleTime;
    private Long handlerId;
    private LocalDateTime alertTime;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
