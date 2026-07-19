-- ============================================================
-- 居家银发多模态健康监护系统 - 数据库设计
-- Database: silver_health
-- ============================================================

CREATE DATABASE IF NOT EXISTS silver_health DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE silver_health;

-- -----------------------------------------------------------
-- 1. 用户表（老人、子女、社区管理员统一管理）
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS sys_user (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    openid          VARCHAR(128) COMMENT '微信openid',
    union_id        VARCHAR(128) COMMENT '微信unionid',
    nickname        VARCHAR(64)  COMMENT '昵称',
    real_name       VARCHAR(64)  COMMENT '真实姓名',
    phone           VARCHAR(20)  COMMENT '手机号',
    avatar          VARCHAR(512) COMMENT '头像URL',
    gender          TINYINT DEFAULT 0 COMMENT '0未知 1男 2女',
    age             INT          COMMENT '年龄',
    id_card         VARCHAR(20)  COMMENT '身份证号',
    address         VARCHAR(256) COMMENT '居住地址',
    user_type       TINYINT NOT NULL DEFAULT 1 COMMENT '1老人 2子女 3社区管理员',
    community_id    BIGINT       COMMENT '所属社区ID',
    is_alone        TINYINT DEFAULT 0 COMMENT '是否独居 0否 1是',
    is_disabled     TINYINT DEFAULT 0 COMMENT '是否失能 0否 1是',
    is_low_income   TINYINT DEFAULT 0 COMMENT '是否低保 0否 1是',
    status          TINYINT DEFAULT 1 COMMENT '0禁用 1正常',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_openid (openid),
    INDEX idx_user_type (user_type),
    INDEX idx_community (community_id)
) ENGINE=InnoDB COMMENT='系统用户表';

-- -----------------------------------------------------------
-- 2. 社区信息表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS community (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(128) NOT NULL COMMENT '社区名称',
    address         VARCHAR(256) COMMENT '社区地址',
    contact_phone   VARCHAR(20)  COMMENT '联系电话',
    grid_worker     VARCHAR(64)  COMMENT '网格员姓名',
    elder_count     INT DEFAULT 0 COMMENT '老年人口数',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB COMMENT='社区信息表';

-- -----------------------------------------------------------
-- 3. 亲属/监护人绑定关系表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS guardian_bindng (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    elder_id        BIGINT NOT NULL COMMENT '老人用户ID',
    guardian_id     BIGINT NOT NULL COMMENT '监护人用户ID（子女/社区管理员）',
    guardian_type   TINYINT NOT NULL DEFAULT 1 COMMENT '1子女 2社区管理员',
    permission      TINYINT DEFAULT 1 COMMENT '1仅查看 2接收告警 3全部权限',
    bind_code       VARCHAR(64)  COMMENT '绑定码',
    status          TINYINT DEFAULT 1 COMMENT '0已解绑 1绑定中',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_elder (elder_id),
    INDEX idx_guardian (guardian_id)
) ENGINE=InnoDB COMMENT='监护人绑定关系表';

-- -----------------------------------------------------------
-- 4. 设备权限状态表（记录老人端硬件权限开关状态）
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS device_permission (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT NOT NULL COMMENT '老人用户ID',
    camera_enabled  TINYINT DEFAULT 0 COMMENT '相机权限 0关 1开',
    mic_enabled     TINYINT DEFAULT 0 COMMENT '麦克风权限 0关 1开',
    screen_on       TINYINT DEFAULT 0 COMMENT '屏幕常亮 0关 1开',
    low_power_mode  TINYINT DEFAULT 0 COMMENT '低功耗模式 0关 1开',
    local_notify    TINYINT DEFAULT 1 COMMENT '本地提醒 0关 1开',
    sms_notify      TINYINT DEFAULT 0 COMMENT '短信紧急通知 0关 1开',
    monitor_status  TINYINT DEFAULT 0 COMMENT '监护状态 0关闭 1开启',
    privacy_agreed  TINYINT DEFAULT 0 COMMENT '是否同意隐私协议',
    no_remind       TINYINT DEFAULT 0 COMMENT '不再提醒权限弹窗',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE INDEX idx_user (user_id)
) ENGINE=InnoDB COMMENT='设备权限状态表';

-- -----------------------------------------------------------
-- 5. 健康日报表（每日汇总）
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS health_daily_report (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT NOT NULL COMMENT '老人用户ID',
    report_date     DATE NOT NULL COMMENT '报告日期',
    activity_minutes INT DEFAULT 0 COMMENT '活动时长(分钟)',
    sedentary_minutes INT DEFAULT 0 COMMENT '久坐时长(分钟)',
    outdoor_count   INT DEFAULT 0 COMMENT '外出次数',
    sleep_time      TIME COMMENT '入睡时间',
    wake_time       TIME COMMENT '起床时间',
    night_wake_count INT DEFAULT 0 COMMENT '夜间起夜次数',
    sleep_score     INT DEFAULT 0 COMMENT '睡眠质量评分(0-100)',
    emotion_status  TINYINT DEFAULT 1 COMMENT '情绪状态 1平稳 2轻微低落 3消极倾向',
    emotion_advice  VARCHAR(512) COMMENT '情绪建议文字',
    overall_score   INT DEFAULT 0 COMMENT '综合健康评分(0-100)',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_date (user_id, report_date)
) ENGINE=InnoDB COMMENT='健康日报表';

-- -----------------------------------------------------------
-- 6. 健康周报/月报表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS health_period_report (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT NOT NULL COMMENT '老人用户ID',
    report_type     TINYINT NOT NULL COMMENT '1周报 2月报',
    start_date      DATE NOT NULL,
    end_date        DATE NOT NULL,
    avg_activity    INT DEFAULT 0 COMMENT '平均每日活动时长',
    avg_sleep_score INT DEFAULT 0 COMMENT '平均睡眠评分',
    emotion_trend   VARCHAR(512) COMMENT '情绪趋势描述',
    alert_count     INT DEFAULT 0 COMMENT '预警事件次数',
    summary         TEXT COMMENT '总结建议',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_period (user_id, report_type, start_date)
) ENGINE=InnoDB COMMENT='健康周报/月报表';

-- -----------------------------------------------------------
-- 7. 风险告警记录表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS risk_alert (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT NOT NULL COMMENT '老人用户ID',
    alert_type      TINYINT NOT NULL COMMENT '1疑似跌倒 2长时间不动 3情绪低落 4夜间异响 5长时间静坐 6作息异常',
    risk_level      TINYINT NOT NULL COMMENT '1低风险(黄色) 2中风险(橙色) 3高风险(红色)',
    description     VARCHAR(512) COMMENT '告警描述',
    notified_family TINYINT DEFAULT 0 COMMENT '是否已通知子女',
    notified_community TINYINT DEFAULT 0 COMMENT '是否已通知社区',
    handle_status   TINYINT DEFAULT 0 COMMENT '0未处理 1已处理 2误报已取消',
    handle_result   VARCHAR(512) COMMENT '处置结果',
    handle_time     DATETIME COMMENT '处置时间',
    handler_id      BIGINT COMMENT '处置人ID',
    alert_time      DATETIME NOT NULL COMMENT '告警发生时间',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_time (user_id, alert_time),
    INDEX idx_risk_level (risk_level),
    INDEX idx_handle_status (handle_status)
) ENGINE=InnoDB COMMENT='风险告警记录表';

-- -----------------------------------------------------------
-- 8. 上门帮扶记录表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS visit_record (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    elder_id        BIGINT NOT NULL COMMENT '老人ID',
    worker_id       BIGINT NOT NULL COMMENT '社区工作人员ID',
    visit_type      TINYINT DEFAULT 1 COMMENT '1日常巡访 2告警响应 3帮扶服务',
    visit_time      DATETIME NOT NULL COMMENT '上门时间',
    content         TEXT COMMENT '帮扶/回访内容',
    result          VARCHAR(512) COMMENT '回访结果',
    alert_id        BIGINT COMMENT '关联告警ID',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_elder (elder_id),
    INDEX idx_worker (worker_id)
) ENGINE=InnoDB COMMENT='上门帮扶记录表';

-- -----------------------------------------------------------
-- 9. 会员服务表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS membership (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT NOT NULL COMMENT '用户ID',
    plan_type       TINYINT NOT NULL COMMENT '0免费版 1月度会员 2年度会员',
    price           DECIMAL(10,2) DEFAULT 0 COMMENT '支付金额',
    start_date      DATE COMMENT '会员开始日期',
    end_date        DATE COMMENT '会员结束日期',
    status          TINYINT DEFAULT 1 COMMENT '0过期 1有效',
    pay_order_no    VARCHAR(64) COMMENT '支付订单号',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user (user_id)
) ENGINE=InnoDB COMMENT='会员服务表';

-- -----------------------------------------------------------
-- 10. 实时监测状态表（记录当前监测运行状态）
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS monitor_session (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT NOT NULL COMMENT '老人用户ID',
    session_start   DATETIME NOT NULL COMMENT '监测开始时间',
    session_end     DATETIME COMMENT '监测结束时间',
    mode            TINYINT DEFAULT 1 COMMENT '1正常模式 2夜间省电模式',
    status          TINYINT DEFAULT 1 COMMENT '0已结束 1进行中',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_status (user_id, status)
) ENGINE=InnoDB COMMENT='监测会话记录表';

-- -----------------------------------------------------------
-- 11. 子女通知设置表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS family_notify_setting (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT NOT NULL COMMENT '子女用户ID',
    wechat_notify   TINYINT DEFAULT 1 COMMENT '微信服务通知 0关 1开',
    sms_notify      TINYINT DEFAULT 0 COMMENT '短信紧急通知 0关 1开',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE INDEX idx_user (user_id)
) ENGINE=InnoDB COMMENT='子女通知设置表';

-- -----------------------------------------------------------
-- 12. 操作日志表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS operation_log (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT COMMENT '操作人ID',
    action          VARCHAR(128) NOT NULL COMMENT '操作动作',
    target_type     VARCHAR(64) COMMENT '操作对象类型',
    target_id       BIGINT COMMENT '操作对象ID',
    detail          TEXT COMMENT '详情',
    ip              VARCHAR(64) COMMENT 'IP地址',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_time (created_at)
) ENGINE=InnoDB COMMENT='操作日志表';
