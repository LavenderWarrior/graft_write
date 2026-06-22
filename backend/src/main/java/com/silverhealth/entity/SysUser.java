package com.silverhealth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String openid;
    private String unionId;
    private String nickname;
    private String realName;
    private String phone;
    private String avatar;
    private Integer gender;
    private Integer age;
    private String idCard;
    private String address;
    /** 1老人 2子女 3社区管理员 */
    private Integer userType;
    private Long communityId;
    private Integer isAlone;
    private Integer isDisabled;
    private Integer isLowIncome;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
