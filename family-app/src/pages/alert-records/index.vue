<template>
  <view class="alerts-page">
    <!-- 筛选栏 -->
    <view class="filter-bar">
      <view class="filter-item" :class="{ active: filter === 'all' }" @tap="setFilter('all')">全部</view>
      <view class="filter-item level-high" :class="{ active: filter === '3' }" @tap="setFilter('3')">高危</view>
      <view class="filter-item level-mid" :class="{ active: filter === '2' }" @tap="setFilter('2')">中危</view>
      <view class="filter-item level-low" :class="{ active: filter === '1' }" @tap="setFilter('1')">低危</view>
    </view>

    <!-- 告警列表 -->
    <view class="alert-list">
      <view v-if="filteredAlerts.length === 0" class="empty-state">
        <text class="empty-icon">✅</text>
        <text class="empty-text">暂无告警记录</text>
      </view>

      <view v-for="(alert, idx) in filteredAlerts" :key="idx" class="alert-card" @tap="showDetail(alert)">
        <view class="alert-left">
          <view class="risk-badge" :class="'level-' + alert.riskLevel">
            {{ riskLevelText(alert.riskLevel) }}
          </view>
        </view>
        <view class="alert-center">
          <text class="alert-type">{{ alertTypeText(alert.alertType) }}</text>
          <text class="alert-desc">{{ alert.description || '检测到异常事件' }}</text>
          <view class="alert-meta">
            <text class="alert-time">{{ alert.alertTime }}</text>
            <text class="alert-status" :class="'status-' + alert.handleStatus">
              {{ handleStatusText(alert.handleStatus) }}
            </text>
          </view>
        </view>
        <text class="alert-arrow">›</text>
      </view>
    </view>

    <!-- 详情弹窗 -->
    <view class="dialog-mask" v-if="showDetailDialog" @tap="showDetailDialog = false">
      <view class="detail-dialog" @tap.stop>
        <view class="detail-header">
          <view class="risk-badge large" :class="'level-' + selectedAlert.riskLevel">
            {{ riskLevelText(selectedAlert.riskLevel) }}
          </view>
          <text class="detail-type">{{ alertTypeText(selectedAlert.alertType) }}</text>
        </view>
        <view class="detail-info">
          <view class="detail-row">
            <text class="dl">发生时间</text>
            <text class="dv">{{ selectedAlert.alertTime }}</text>
          </view>
          <view class="detail-row">
            <text class="dl">风险类型</text>
            <text class="dv">{{ alertTypeText(selectedAlert.alertType) }}</text>
          </view>
          <view class="detail-row">
            <text class="dl">处置状态</text>
            <text class="dv" :class="'status-' + selectedAlert.handleStatus">
              {{ handleStatusText(selectedAlert.handleStatus) }}
            </text>
          </view>
          <view class="detail-row">
            <text class="dl">已通知社区</text>
            <text class="dv">{{ selectedAlert.notifiedCommunity ? '是' : '否' }}</text>
          </view>
          <view class="detail-row">
            <text class="dl">已联系家属</text>
            <text class="dv">{{ selectedAlert.notifiedFamily ? '是' : '否' }}</text>
          </view>
        </view>
        <view class="detail-close" @tap="showDetailDialog = false">关闭</view>
      </view>
    </view>
  </view>
</template>

<script>
import api from '../../common/api.js'

export default {
  data() {
    return {
      filter: 'all',
      alerts: [
        { id: 1, alertType: 1, riskLevel: 3, alertTime: '2026-06-22 08:30', handleStatus: 1, notifiedFamily: 1, notifiedCommunity: 1, description: '疑似发生跌倒' },
        { id: 2, alertType: 5, riskLevel: 2, alertTime: '2026-06-21 14:20', handleStatus: 0, notifiedFamily: 1, notifiedCommunity: 0, description: '长时间静坐超过3小时' },
        { id: 3, alertType: 3, riskLevel: 2, alertTime: '2026-06-20 19:45', handleStatus: 1, notifiedFamily: 1, notifiedCommunity: 0, description: '情绪低落，语音分析检测' },
        { id: 4, alertType: 6, riskLevel: 1, alertTime: '2026-06-19 07:00', handleStatus: 1, notifiedFamily: 0, notifiedCommunity: 0, description: '作息时间异常，起床时间偏晚' },
        { id: 5, alertType: 4, riskLevel: 1, alertTime: '2026-06-18 03:15', handleStatus: 2, notifiedFamily: 0, notifiedCommunity: 0, description: '夜间异响检测' }
      ],
      showDetailDialog: false,
      selectedAlert: {}
    }
  },
  computed: {
    filteredAlerts() {
      if (this.filter === 'all') return this.alerts
      return this.alerts.filter(a => a.riskLevel === Number(this.filter))
    }
  },
  methods: {
    setFilter(f) { this.filter = f },
    riskLevelText(level) {
      const map = { 1: '低危', 2: '中危', 3: '高危' }
      return map[level] || '未知'
    },
    alertTypeText(type) {
      const map = { 1: '疑似跌倒', 2: '长时间不动', 3: '情绪低落', 4: '夜间异响', 5: '长时间静坐', 6: '作息异常' }
      return map[type] || '异常'
    },
    handleStatusText(status) {
      const map = { 0: '未处理', 1: '已处理', 2: '误报取消' }
      return map[status] || '未知'
    },
    showDetail(alert) {
      this.selectedAlert = alert
      this.showDetailDialog = true
    }
  }
}
</script>

<style scoped>
.alerts-page { min-height: 100vh; background: #f5f5f5; padding-bottom: 140rpx; }

.filter-bar {
  display: flex; background: #fff; padding: 16rpx 24rpx; gap: 12rpx;
}
.filter-item {
  flex: 1; text-align: center; padding: 14rpx 0; border-radius: 12rpx;
  font-size: 28rpx; color: #999; background: #f5f5f5;
}
.filter-item.active { color: #fff; font-weight: bold; }
.filter-item.active:not(.level-high):not(.level-mid):not(.level-low) { background: #2196F3; }
.filter-item.level-high.active { background: #F44336; }
.filter-item.level-mid.active { background: #FF9800; }
.filter-item.level-low.active { background: #FFC107; color: #333; }

.alert-list { padding: 16rpx 24rpx; }

.empty-state { text-align: center; padding: 80rpx 0; }
.empty-icon { display: block; font-size: 80rpx; margin-bottom: 16rpx; }
.empty-text { font-size: 30rpx; color: #ccc; }

.alert-card {
  display: flex; align-items: center; background: #fff;
  border-radius: 20rpx; padding: 24rpx; margin-bottom: 16rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.04);
}
.risk-badge {
  padding: 8rpx 16rpx; border-radius: 12rpx; font-size: 24rpx;
  font-weight: bold; white-space: nowrap;
}
.risk-badge.level-1 { background: #FFF8E1; color: #F57F17; }
.risk-badge.level-2 { background: #FFF3E0; color: #E65100; }
.risk-badge.level-3 { background: #FFEBEE; color: #C62828; }
.risk-badge.large { font-size: 30rpx; padding: 12rpx 24rpx; }

.alert-left { margin-right: 16rpx; }
.alert-center { flex: 1; }
.alert-type { display: block; font-size: 30rpx; font-weight: bold; color: #333; }
.alert-desc { display: block; font-size: 26rpx; color: #999; margin-top: 4rpx; }
.alert-meta { display: flex; align-items: center; margin-top: 8rpx; gap: 16rpx; }
.alert-time { font-size: 24rpx; color: #bbb; }
.alert-status { font-size: 24rpx; padding: 2rpx 12rpx; border-radius: 8rpx; }
.status-0 { background: #FFEBEE; color: #C62828; }
.status-1 { background: #E8F5E9; color: #2E7D32; }
.status-2 { background: #f5f5f5; color: #999; }
.alert-arrow { font-size: 36rpx; color: #ccc; }

.dialog-mask {
  position: fixed; top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.5); display: flex;
  align-items: center; justify-content: center; z-index: 999;
}
.detail-dialog {
  width: 620rpx; background: #fff; border-radius: 24rpx; padding: 40rpx;
}
.detail-header { text-align: center; margin-bottom: 28rpx; }
.detail-type { display: block; font-size: 36rpx; font-weight: bold; color: #333; margin-top: 12rpx; }
.detail-info { margin-bottom: 28rpx; }
.detail-row {
  display: flex; justify-content: space-between; padding: 16rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}
.dl { font-size: 28rpx; color: #999; }
.dv { font-size: 28rpx; color: #333; font-weight: bold; }
.detail-close {
  text-align: center; padding: 16rpx; font-size: 30rpx; color: #999;
}
</style>
