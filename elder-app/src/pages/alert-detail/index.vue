<template>
  <view class="detail-page">
    <view class="card detail-card">
      <!-- 风险等级标识 -->
      <view class="level-badge" :class="'level-' + alert.riskLevel">
        {{ levelText }}
      </view>

      <!-- 基本信息 -->
      <view class="info-section">
        <view class="info-row">
          <text class="info-label">异常类型</text>
          <text class="info-value">{{ alertTypeText }}</text>
        </view>
        <view class="info-row">
          <text class="info-label">发生时间</text>
          <text class="info-value">{{ alert.alertTime || '--' }}</text>
        </view>
        <view class="info-row">
          <text class="info-label">风险等级</text>
          <text class="info-value" :class="'text-level-' + alert.riskLevel">{{ levelText }}</text>
        </view>
        <view class="info-row">
          <text class="info-label">处置状态</text>
          <text class="info-value">{{ handleStatusText }}</text>
        </view>
        <view class="info-row">
          <text class="info-label">通知子女</text>
          <text class="info-value">{{ alert.notifiedFamily ? '已通知' : '未通知' }}</text>
        </view>
        <view class="info-row">
          <text class="info-label">通知社区</text>
          <text class="info-value">{{ alert.notifiedCommunity ? '已通知' : '未通知' }}</text>
        </view>
      </view>

      <!-- 描述 -->
      <view class="desc-section" v-if="alert.description">
        <text class="desc-label">详情描述</text>
        <text class="desc-text">{{ alert.description }}</text>
      </view>

      <!-- 处置结果 -->
      <view class="desc-section" v-if="alert.handleResult">
        <text class="desc-label">处置结果</text>
        <text class="desc-text">{{ alert.handleResult }}</text>
      </view>
    </view>

    <!-- 隐私说明 -->
    <view class="privacy-note">
      仅本地文字记录，无任何视频图片存储展示
    </view>

    <!-- 操作按钮 -->
    <view class="action-area">
      <view class="action-btn mark-btn" v-if="alert.handleStatus === 0" @tap="markHandled">
        标记已处理
      </view>
      <view class="action-btn back-btn" @tap="goBack">返回报告</view>
    </view>
  </view>
</template>

<script>
import api from '../../common/api.js'

export default {
  data() {
    return {
      alert: {}
    }
  },
  computed: {
    levelText() {
      const map = { 1: '低风险', 2: '中风险', 3: '高风险' }
      return map[this.alert.riskLevel] || '未知'
    },
    alertTypeText() {
      const map = { 1: '疑似跌倒', 2: '长时间不动', 3: '情绪低落', 4: '夜间异响', 5: '长时间静坐', 6: '作息异常' }
      return map[this.alert.alertType] || '未知异常'
    },
    handleStatusText() {
      const map = { 0: '未处理', 1: '已处理', 2: '误报已取消' }
      return map[this.alert.handleStatus] || '未知'
    }
  },
  methods: {
    markHandled() {
      const userId = uni.getStorageSync('userId')
      api.cancelAlert(this.alert.id, userId).then(() => {
        this.alert.handleStatus = 1
        uni.showToast({ title: '已标记处理', icon: 'success' })
      }).catch(() => {})
    },
    goBack() {
      uni.navigateBack()
    }
  },
  onLoad(options) {
    if (options.id) {
      this.alert = {
        id: options.id,
        alertType: Number(options.type) || 1,
        riskLevel: Number(options.level) || 1,
        alertTime: options.time || '',
        handleStatus: Number(options.status) || 0,
        notifiedFamily: Number(options.notifiedFamily) || 0,
        notifiedCommunity: Number(options.notifiedCommunity) || 0,
        description: options.desc || '',
        handleResult: options.result || ''
      }
    }
  }
}
</script>

<style scoped>
.detail-page { min-height: 100vh; background: #f5f5f5; padding-bottom: 200rpx; }

.detail-card { margin-top: 20rpx; }
.level-badge {
  display: inline-block; padding: 12rpx 32rpx; border-radius: 24rpx;
  font-size: 32rpx; font-weight: bold; margin-bottom: 28rpx;
}
.level-badge.level-1 { background: #FFF8E1; color: #F57F17; }
.level-badge.level-2 { background: #FFF3E0; color: #E65100; }
.level-badge.level-3 { background: #FFEBEE; color: #C62828; }

.info-section { margin-bottom: 24rpx; }
.info-row {
  display: flex; justify-content: space-between; align-items: center;
  padding: 20rpx 0; border-bottom: 1rpx solid #f5f5f5;
}
.info-label { font-size: 32rpx; color: #999; }
.info-value { font-size: 32rpx; color: #333; font-weight: bold; }
.text-level-1 { color: #F57F17; }
.text-level-2 { color: #E65100; }
.text-level-3 { color: #C62828; }

.desc-section { margin-top: 24rpx; }
.desc-label { display: block; font-size: 32rpx; color: #999; margin-bottom: 12rpx; }
.desc-text { display: block; font-size: 32rpx; color: #555; line-height: 1.6; }

.privacy-note {
  text-align: center; font-size: 26rpx; color: #2E7D32;
  padding: 20rpx; margin: 20rpx 24rpx;
  background: #E8F5E9; border-radius: 12rpx;
}

.action-area {
  position: fixed; bottom: 0; left: 0; right: 0;
  padding: 24rpx 48rpx 48rpx; background: #fff;
  display: flex; gap: 20rpx;
  box-shadow: 0 -4rpx 16rpx rgba(0,0,0,0.06);
}
.action-btn {
  flex: 1; text-align: center; padding: 26rpx 0;
  border-radius: 24rpx; font-size: 34rpx; font-weight: bold;
}
.mark-btn { background: #4CAF50; color: #fff; }
.back-btn { background: #f5f5f5; color: #666; }
</style>
