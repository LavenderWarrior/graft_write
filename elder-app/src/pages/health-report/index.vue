<template>
  <view class="report-page">
    <!-- 顶部切换标签 -->
    <view class="tab-bar">
      <view class="tab-item" :class="{ active: activeTab === 'today' }" @tap="switchTab('today')">今日报告</view>
      <view class="tab-item" :class="{ active: activeTab === 'week' }" @tap="switchTab('week')">本周报告</view>
      <view class="tab-item" :class="{ active: activeTab === 'month' }" @tap="switchTab('month')">月度总结</view>
    </view>

    <!-- 活动行为记录 -->
    <view class="card section-card">
      <view class="section-header">
        <text class="section-icon">🏃</text>
        <text class="section-title">活动行为记录</text>
      </view>
      <view class="data-row">
        <view class="data-item">
          <text class="data-label">活动时长</text>
          <view class="data-block green">{{ reportData.activityMinutes || 0 }}分钟</view>
        </view>
        <view class="data-item">
          <text class="data-label">久坐时长</text>
          <view class="data-block orange">{{ reportData.sedentaryMinutes || 0 }}分钟</view>
        </view>
        <view class="data-item">
          <text class="data-label">外出记录</text>
          <view class="data-block blue">{{ reportData.outdoorCount || 0 }}次</view>
        </view>
      </view>
    </view>

    <!-- 睡眠监测数据 -->
    <view class="card section-card">
      <view class="section-header">
        <text class="section-icon">😴</text>
        <text class="section-title">睡眠监测数据</text>
      </view>
      <view class="data-row">
        <view class="data-item">
          <text class="data-label">入睡时间</text>
          <view class="data-block purple">{{ reportData.sleepTime || '--:--' }}</view>
        </view>
        <view class="data-item">
          <text class="data-label">起夜次数</text>
          <view class="data-block orange">{{ reportData.nightWakeCount || 0 }}次</view>
        </view>
        <view class="data-item">
          <text class="data-label">睡眠评分</text>
          <view class="data-block" :class="sleepScoreClass">{{ reportData.sleepScore || '--' }}分</view>
        </view>
      </view>
    </view>

    <!-- 情绪心理评估 -->
    <view class="card section-card">
      <view class="section-header">
        <text class="section-icon">💭</text>
        <text class="section-title">情绪心理评估</text>
      </view>
      <view class="emotion-display">
        <view class="emotion-badge" :class="'emotion-' + (reportData.emotionStatus || 1)">
          {{ emotionText }}
        </view>
        <text class="emotion-advice">{{ reportData.emotionAdvice || '今日情绪状态良好，继续保持积极的生活态度。' }}</text>
      </view>
    </view>

    <!-- 历史异常记录 -->
    <view class="card section-card">
      <view class="section-header">
        <text class="section-icon">⚠️</text>
        <text class="section-title">历史异常记录</text>
      </view>
      <view v-if="alerts.length === 0" class="empty-text">暂无异常记录</view>
      <view v-for="(alert, idx) in alerts" :key="idx" class="alert-item"
            @tap="goAlertDetail(alert)">
        <view class="alert-dot" :class="'level-' + alert.riskLevel"></view>
        <view class="alert-info">
          <text class="alert-type">{{ alertTypeText(alert.alertType) }}</text>
          <text class="alert-time">{{ alert.alertTime }}</text>
        </view>
        <text class="alert-arrow">›</text>
      </view>
    </view>
  </view>
</template>

<script>
import api from '../../common/api.js'

export default {
  data() {
    return {
      activeTab: 'today',
      reportData: {},
      alerts: []
    }
  },
  computed: {
    emotionText() {
      const map = { 1: '情绪平稳', 2: '轻微低落', 3: '消极倾向' }
      return map[this.reportData.emotionStatus] || '情绪平稳'
    },
    sleepScoreClass() {
      const score = this.reportData.sleepScore || 0
      if (score >= 80) return 'green'
      if (score >= 60) return 'orange'
      return 'red'
    }
  },
  methods: {
    switchTab(tab) {
      this.activeTab = tab
      this.loadData()
    },
    alertTypeText(type) {
      const map = { 1: '疑似跌倒', 2: '长时间不动', 3: '情绪低落', 4: '夜间异响', 5: '长时间静坐', 6: '作息异常' }
      return map[type] || '未知异常'
    },
    loadData() {
      const userId = uni.getStorageSync('userId')
      if (!userId) return
      if (this.activeTab === 'today') {
        api.getTodayReport(userId).then(data => {
          this.reportData = data || {}
        }).catch(() => {})
      }
      api.getRecentAlerts(userId, 10).then(data => {
        this.alerts = data || []
      }).catch(() => {})
    },
    goAlertDetail(alert) {
      uni.navigateTo({
        url: `/pages/alert-detail/index?id=${alert.id}`
      })
    }
  },
  onShow() {
    this.loadData()
  }
}
</script>

<style scoped>
.report-page { min-height: 100vh; background: #f5f5f5; padding-bottom: 140rpx; }

.tab-bar {
  display: flex; background: #fff; padding: 0 24rpx;
  border-bottom: 2rpx solid #eee;
}
.tab-item {
  flex: 1; text-align: center; padding: 28rpx 0; font-size: 32rpx;
  color: #999; position: relative;
}
.tab-item.active {
  color: #4CAF50; font-weight: bold;
}
.tab-item.active::after {
  content: ''; position: absolute; bottom: 0; left: 25%; right: 25%;
  height: 6rpx; background: #4CAF50; border-radius: 3rpx;
}

.section-card { margin-top: 20rpx; }
.section-header { display: flex; align-items: center; margin-bottom: 24rpx; }
.section-icon { font-size: 40rpx; margin-right: 12rpx; }
.section-title { font-size: 34rpx; font-weight: bold; color: #333; }

.data-row { display: flex; gap: 16rpx; }
.data-item { flex: 1; text-align: center; }
.data-label { display: block; font-size: 28rpx; color: #999; margin-bottom: 12rpx; }
.data-block {
  padding: 16rpx 8rpx; border-radius: 16rpx; font-size: 32rpx; font-weight: bold;
}
.data-block.green { background: #E8F5E9; color: #2E7D32; }
.data-block.orange { background: #FFF3E0; color: #E65100; }
.data-block.blue { background: #E3F2FD; color: #1565C0; }
.data-block.purple { background: #F3E5F5; color: #7B1FA2; }
.data-block.red { background: #FFEBEE; color: #C62828; }

.emotion-display { text-align: center; }
.emotion-badge {
  display: inline-block; padding: 16rpx 48rpx; border-radius: 40rpx;
  font-size: 36rpx; font-weight: bold; margin-bottom: 20rpx;
}
.emotion-badge.emotion-1 { background: #E8F5E9; color: #2E7D32; }
.emotion-badge.emotion-2 { background: #FFF3E0; color: #E65100; }
.emotion-badge.emotion-3 { background: #FFEBEE; color: #C62828; }
.emotion-advice { display: block; font-size: 30rpx; color: #666; line-height: 1.6; }

.empty-text { text-align: center; color: #ccc; font-size: 30rpx; padding: 32rpx 0; }

.alert-item {
  display: flex; align-items: center; padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}
.alert-item:last-child { border-bottom: none; }
.alert-dot {
  width: 16rpx; height: 16rpx; border-radius: 50%; margin-right: 16rpx;
}
.alert-dot.level-1 { background: #FFC107; }
.alert-dot.level-2 { background: #FF9800; }
.alert-dot.level-3 { background: #F44336; }
.alert-info { flex: 1; }
.alert-type { display: block; font-size: 32rpx; color: #333; }
.alert-time { display: block; font-size: 26rpx; color: #999; margin-top: 4rpx; }
.alert-arrow { font-size: 40rpx; color: #ccc; }
</style>
