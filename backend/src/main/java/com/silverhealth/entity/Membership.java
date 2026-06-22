package com.silverhealth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("membership")
public class Membership {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    /** 0免费版 1月度会员 2年度会员 */
    private Integer planType;
    private BigDecimal price;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer status;
    private String payOrderNo;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
