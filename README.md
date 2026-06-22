# 居家银发多模态健康监护系统

基于手机摄像头和麦克风的非穿戴式居家老人健康监护AI系统，涵盖老人端、子女监护端和社区管理后台三大平台。

## 技术栈

| 模块 | 技术 |
|------|------|
| 后端服务 | Spring Boot 2.7 + MyBatis-Plus + MySQL |
| 老人端小程序 | UniApp (Vue 2/3, 适配微信小程序) |
| 子女监护端小程序 | UniApp (Vue 2/3, 适配微信小程序) |
| 社区管理后台 | Vue 3 + Element Plus + ECharts + Vite |

## 项目结构

```
├── backend/                  # Spring Boot 后端
│   ├── src/main/java/com/silverhealth/
│   │   ├── controller/       # REST API 控制器
│   │   ├── service/          # 业务逻辑层
│   │   ├── mapper/           # MyBatis-Plus Mapper
│   │   ├── entity/           # 数据实体类
│   │   ├── common/           # 通用工具类
│   │   └── config/           # 配置类
│   └── src/main/resources/
│       ├── application.yml   # 配置文件
│       └── schema.sql        # 数据库建表脚本
├── elder-app/                # 老人端 UniApp
│   ├── pages/                # 9个页面
│   │   ├── welcome/          # 启动欢迎页
│   │   ├── permission-guide/ # 权限引导页
│   │   ├── home/             # 首页（核心）
│   │   ├── monitor/          # 实时监护取景页
│   │   ├── health-report/    # 健康报告Tab页
│   │   ├── settings/         # 我的设置Tab页
│   │   ├── binding/          # 亲属&社区绑定页
│   │   ├── membership/       # 会员服务开通页
│   │   └── alert-detail/     # 异常日志详情页
│   └── components/           # 6个弹窗组件
│       ├── privacy-dialog    # 隐私告知确认弹窗
│       ├── camera-permission-dialog # 相机权限弹窗
│       ├── mic-permission-dialog    # 麦克风权限弹窗
│       ├── risk-alert-dialog # 三级风险告警弹窗
│       ├── sos-confirm-dialog       # 一键呼救确认弹窗
│       └── monitor-stop-dialog      # 监护启停确认弹窗
├── family-app/               # 子女监护端 UniApp
│   ├── pages/
│   │   ├── login/            # 登录/绑定页
│   │   ├── home/             # 首页
│   │   ├── health-data/      # 健康数据详情页
│   │   ├── alert-records/    # 告警记录页
│   │   └── profile/          # 个人设置页
└── admin-web/                # 社区管理后台 (Vue3 + Element Plus)
    └── src/views/
        ├── dashboard/        # PC管理大屏数据看板
        ├── elders/           # 辖区老人档案列表
        ├── elder-detail/     # 老人详情管理页
        ├── alerts/           # 告警处置台账
        └── visit-records/    # 上门帮扶记录
```

## 快速开始

### 1. 数据库初始化
```bash
mysql -u root -p < backend/src/main/resources/schema.sql
```

### 2. 启动后端
```bash
cd backend
mvn spring-boot:run
```

### 3. 启动老人端/子女端（开发模式）
```bash
# 安装 HBuilderX 或使用 CLI
cd elder-app   # 或 family-app
# 使用 HBuilderX 导入项目并运行到微信小程序/H5
```

### 4. 启动社区管理后台
```bash
cd admin-web
npm install
npm run dev
```

## 核心功能

### 老人端
- 非穿戴式摄像头跌倒/睡眠监测
- 麦克风情绪分析和夜间异响检测
- 三级风险告警（黄/橙/红）
- 一键紧急呼救
- 隐私本地处理，数据不上云

### 子女监护端
- 远程查看老人健康状态
- 实时接收风险推送通知
- 健康数据可视化（活动/睡眠/情绪）
- 多老人管理

### 社区管理后台
- 辖区老人总览数据看板
- 老人档案管理
- 告警处置台账
- 上门帮扶记录
- 数据可视化大屏（ECharts）
- 月度报表导出

## 数据库表说明

| 表名 | 说明 |
|------|------|
| sys_user | 用户表（老人/子女/管理员） |
| community | 社区信息表 |
| guardian_bindng | 监护人绑定关系表 |
| device_permission | 设备权限状态表 |
| health_daily_report | 健康日报表 |
| health_period_report | 健康周报/月报表 |
| risk_alert | 风险告警记录表 |
| visit_record | 上门帮扶记录表 |
| membership | 会员服务表 |
| monitor_session | 监测会话记录表 |
| family_notify_setting | 子女通知设置表 |
| operation_log | 操作日志表 |
