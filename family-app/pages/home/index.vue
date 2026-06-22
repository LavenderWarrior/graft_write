<template>
  <view class="home-page">
    <!-- 顶部老人信息 -->
    <view class="elder-header">
      <view class="elder-info">
        <text class="elder-avatar">👴</text>
        <view class="elder-detail">
          <text class="elder-name">{{ elderName }}</text>
          <view class="elder-status" :class="{ online: monitorOnline }">
            <view class="status-dot"></view>
            <text>{{ monitorOnline ? '监护在线' : '监护已关闭' }}</text>
          </view>
        </view>
      </view>
      <view class="elder-switch" v-if="elders.length > 1" @tap="showElderPicker = true">
        切换 ›
      </view>
    </view>

    <!-- 未处理风险提醒（置顶） -->
    <view class="alert-card" v-if="unhandledAlerts.length > 0">
      <view class="alert-header">
        <text class="alert-icon">🚨</text>
        <text class="alert-title">未处理风险提醒</text>
        <text class="alert-count">{{ unhandledAlerts.length }}条</text>
      </view>
      <view v-for="(alert, idx) in unhandledAlerts.slice(0, 3)" :key="idx" class="alert-item">
        <view class="alert-dot" :class="'level-' + alert.riskLevel"></view>
        <text class="alert-desc">{{ alert.description || alertTypeText(alert.alertType) }}</text>
        <text class="alert-time">{{ formatTime(alert.alertTime) }}</text>
      </view>
    </view>

    <!-- 今日健康速览 -->
    <view class="section-title">今日健康速览</view>
    <view class="health-cards">
      <view class="health-card activity">
        <text class="hc-icon">🏃</text>
        <text class="hc-value">{{ todayReport.activityMinutes || 0 }}</text>
        <text class="hc-unit">分钟</text>
        <text class="hc-label">活动时长</text>
      </view>
      <view class="health-card sleep">
        <text class="hc-icon">😴</text>
        <text class="hc-value">{{ todayReport.sleepScore || '--' }}</text>
        <text class="hc-unit">分</text>
        <text class="hc-label">睡眠评分</text>
      </view>
      <view class="health-card emotion">
        <text class="hc-icon">💭</text>
        <text class="hc-value emotion-text" :class="'emotion-' + (todayReport.emotionStatus || 1)">
          {{ emotionText }}
        </text>
        <text class="hc-label">情绪状态</text>
      </view>
    </view>

    <!-- 快捷功能 -->
    <view class="section-title">快捷功能</view>
    <view class="quick-actions">
      <view class="quick-btn" @tap="goWeekReport">
        <text class="qb-icon">📊</text>
        <text class="qb-text">查看完整周报</text>
      </view>
      <view class="quick-btn" @tap="contactCommunity">
        <text class="qb-icon">🏘️</text>
        <text class="qb-text">联系社区</text>
      </view>
      <view class="quick-btn" @tap="sendReminder">
        <text class="qb-icon">💌</text>
        <text class="qb-text">给老人发提醒</text>
      </view>
    </view>

    <!-- 老人选择弹窗 -->
    <view class="dialog-mask" v-if="showElderPicker" @tap="showElderPicker = false">
      <view class="picker-dialog" @tap.stop>
        <view class="picker-title">选择老人</view>
        <view v-for="(elder, idx) in elders" :key="idx"
              class="picker-item" :class="{ active: currentElderIdx === idx }"
              @tap="switchElder(idx)">
          <text class="picker-avatar">👴</text>
          <text class="picker-name">{{ elder.realName || '老人' + (idx + 1) }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import api from '../../common/api.js'

export default {
  data() {
    return {
      elderName: '家中老人',
      monitorOnline: false,
      elders: [],
      currentElderIdx: 0,
      unhandledAlerts: [],
      todayReport: {},
      showElderPicker: false
    }
  },
  computed: {
    emotionText() {
      const map = { 1: '平稳', 2: '轻微低落', 3: '消极倾向' }
      return map[this.todayReport.emotionStatus] || '平稳'
    }
  },
  methods: {
    alertTypeText(type) {
      const map = { 1: '疑似跌倒', 2: '长时间不动', 3: '情绪低落', 4: '夜间异响', 5: '长时间静坐', 6: '作息异常' }
      return map[type] || '异常事件'
    },
    formatTime(time) {
      if (!time) return ''
      return time.substring(11, 16)
    },
    switchElder(idx) {
      this.currentElderIdx = idx
      this.showElderPicker = false
      this.loadElderData()
    },
    goWeekReport() {
      uni.switchTab({ url: '/pages/health-data/index' })
    },
    contactCommunity() {
      uni.makePhoneCall({ phoneNumber: '12345' })
    },
    sendReminder() {
      uni.showModal({
        title: '发送提醒',
        content: '给老人发送一条关怀提醒？',
        success: (res) => {
          if (res.confirm) {
            uni.showToast({ title: '提醒已发送', icon: 'success' })
          }
        }
      })
    },
    loadElderData() {
      const guardianId = uni.getStorageSync('familyUserId')
      if (!guardianId) return
      api.getBoundElders(guardianId).then(data => {
        this.elders = data || []
        if (this.elders.length > 0) {
          const elder = this.elders[this.currentElderIdx]
          this.elderName = elder.realName || '家中老人'
          this.loadHealthData(elder.id)
          this.loadAlerts(elder.id)
        }
      }).catch(() => {})
    },
    loadHealthData(elderId) {
      api.getTodayReport(elderId).then(data => {
        this.todayReport = data || {}
      }).catch(() => {})
    },
    loadAlerts(elderId) {
      api.getRecentAlerts(elderId, 5).then(data => {
        this.unhandledAlerts = (data || []).filter(a => a.handleStatus === 0)
      }).catch(() => {})
    }
  },
  onShow() {
    this.loadElderData()
  },
  onPullDownRefresh() {
    this.loadElderData()
    setTimeout(() => { uni.stopPullDownRefresh() }, 1000)
  }
}
</script>

<style scoped>
.home-page { min-height: 100vh; background: #f5f5f5; padding-bottom: 140rpx; }

.elder-header {
  background: #fff; padding: 28rpx 24rpx;
  display: flex; align-items: center; justify-content: space-between;
}
.elder-info { display: flex; align-items: center; }
.elder-avatar { font-size: 64rpx; margin-right: 16rpx; }
.elder-name { display: block; font-size: 34rpx; font-weight: bold; color: #333; }
.elder-status {
  display: flex; align-items: center; margin-top: 4rpx;
  font-size: 26rpx; color: #999;
}
.elder-status.online { color: #4CAF50; }
.elder-status .status-dot {
  width: 12rpx; height: 12rpx; border-radius: 50%;
  background: #ccc; margin-right: 8rpx;
}
.elder-status.online .status-dot { background: #4CAF50; }
.elder-switch { font-size: 28rpx; color: #2196F3; }

.alert-card {
  background: #FFF3F3; margin: 16rpx 24rpx; border-radius: 20rpx;
  padding: 24rpx; border: 2rpx solid #FFCDD2;
}
.alert-header { display: flex; align-items: center; margin-bottom: 16rpx; }
.alert-icon { font-size: 36rpx; margin-right: 8rpx; }
.alert-title { font-size: 32rpx; font-weight: bold; color: #C62828; flex: 1; }
.alert-count { font-size: 26rpx; color: #E57373; background: #FFEBEE; padding: 4rpx 12rpx; border-radius: 12rpx; }
.alert-item {
  display: flex; align-items: center; padding: 12rpx 0;
  border-top: 1rpx solid #FFCDD2;
}
.alert-dot { width: 12rpx; height: 12rpx; border-radius: 50%; margin-right: 12rpx; }
.alert-dot.level-1 { background: #FFC107; }
.alert-dot.level-2 { background: #FF9800; }
.alert-dot.level-3 { background: #F44336; }
.alert-desc { flex: 1; font-size: 28rpx; color: #555; }
.alert-time { font-size: 24rpx; color: #999; }

.section-title { padding: 24rpx 24rpx 8rpx; font-size: 32rpx; font-weight: bold; color: #333; }

.health-cards { display: flex; padding: 8rpx 16rpx; gap: 12rpx; }
.health-card {
  flex: 1; background: #fff; border-radius: 20rpx; padding: 24rpx 16rpx;
  text-align: center; box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.04);
}
.hc-icon { display: block; font-size: 48rpx; margin-bottom: 8rpx; }
.hc-value { display: block; font-size: 40rpx; font-weight: bold; color: #333; }
.hc-unit { font-size: 22rpx; color: #999; }
.hc-label { display: block; font-size: 24rpx; color: #999; margin-top: 4rpx; }
.emotion-text { font-size: 30rpx !important; }
.emotion-1 { color: #4CAF50 !important; }
.emotion-2 { color: #FF9800 !important; }
.emotion-3 { color: #F44336 !important; }

.quick-actions { display: flex; padding: 8rpx 16rpx; gap: 12rpx; }
.quick-btn {
  flex: 1; background: #fff; border-radius: 20rpx; padding: 28rpx 16rpx;
  text-align: center; box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.04);
}
.qb-icon { display: block; font-size: 48rpx; margin-bottom: 8rpx; }
.qb-text { display: block; font-size: 26rpx; color: #555; }

.dialog-mask {
  position: fixed; top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.5); display: flex;
  align-items: flex-end; z-index: 999;
}
.picker-dialog {
  width: 100%; background: #fff; border-radius: 28rpx 28rpx 0 0;
  padding: 36rpx 24rpx 60rpx;
}
.picker-title { font-size: 34rpx; font-weight: bold; text-align: center; margin-bottom: 24rpx; }
.picker-item {
  display: flex; align-items: center; padding: 24rpx 16rpx;
  border-radius: 16rpx; margin-bottom: 8rpx;
}
.picker-item.active { background: #E3F2FD; }
.picker-avatar { font-size: 48rpx; margin-right: 16rpx; }
.picker-name { font-size: 32rpx; color: #333; }
</style>
