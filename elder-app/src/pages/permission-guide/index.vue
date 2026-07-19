<template>
  <view class="guide-page">
    <view class="guide-header">
      <text class="guide-title">开启健康监护</text>
      <text class="guide-subtitle">以下功能需要您的授权</text>
    </view>

    <view class="card-list">
      <view class="guide-card">
        <view class="card-icon">📷</view>
        <view class="card-info">
          <text class="card-title">摄像头监测跌倒睡眠</text>
          <text class="card-desc">通过后置摄像头识别行走姿态、跌倒事件、睡眠状态</text>
        </view>
        <view class="card-status" :class="{ active: cameraGranted }">
          {{ cameraGranted ? '已授权' : '待授权' }}
        </view>
      </view>

      <view class="guide-card">
        <view class="card-icon">🎙️</view>
        <view class="card-info">
          <text class="card-title">麦克风分析情绪异响</text>
          <text class="card-desc">采集日常语音分析情绪状态，识别夜间异响</text>
        </view>
        <view class="card-status" :class="{ active: micGranted }">
          {{ micGranted ? '已授权' : '待授权' }}
        </view>
      </view>

      <view class="guide-card">
        <view class="card-icon">🔔</view>
        <view class="card-info">
          <text class="card-title">三方联动安全预警</text>
          <text class="card-desc">老人、子女、社区三方协同，风险即时推送</text>
        </view>
        <view class="card-status active">已就绪</view>
      </view>
    </view>

    <view class="privacy-bar">
      所有画面和语音仅本地AI运算，不上传云端
    </view>

    <view class="bottom-area">
      <view class="next-btn" @tap="handleNext">下一步</view>
    </view>

    <camera-permission-dialog
      :visible="showCameraDialog"
      @allow="onCameraAllow"
      @refuse="onCameraRefuse"
    />
    <mic-permission-dialog
      :visible="showMicDialog"
      @allow="onMicAllow"
      @refuse="onMicRefuse"
    />
  </view>
</template>

<script>
import CameraPermissionDialog from '../../components/camera-permission-dialog.vue'
import MicPermissionDialog from '../../components/mic-permission-dialog.vue'

export default {
  components: { CameraPermissionDialog, MicPermissionDialog },
  data() {
    return {
      cameraGranted: false,
      micGranted: false,
      showCameraDialog: false,
      showMicDialog: false
    }
  },
  methods: {
    handleNext() {
      this.showCameraDialog = true
    },
    onCameraAllow() {
      this.cameraGranted = true
      this.showCameraDialog = false
      uni.setStorageSync('cameraEnabled', true)
      setTimeout(() => { this.showMicDialog = true }, 300)
    },
    onCameraRefuse() {
      this.showCameraDialog = false
      uni.setStorageSync('cameraEnabled', false)
      setTimeout(() => { this.showMicDialog = true }, 300)
    },
    onMicAllow() {
      this.micGranted = true
      this.showMicDialog = false
      uni.setStorageSync('micEnabled', true)
      this.goHome()
    },
    onMicRefuse() {
      this.showMicDialog = false
      uni.setStorageSync('micEnabled', false)
      this.goHome()
    },
    goHome() {
      uni.setStorageSync('permissionGuided', true)
      uni.switchTab({ url: '/pages/home/index' })
    }
  }
}
</script>

<style scoped>
.guide-page {
  min-height: 100vh; background: #f5f5f5;
  display: flex; flex-direction: column; padding-bottom: 160rpx;
}
.guide-header { padding: 60rpx 40rpx 32rpx; text-align: center; }
.guide-title { display: block; font-size: 44rpx; font-weight: bold; color: #333; margin-bottom: 12rpx; }
.guide-subtitle { display: block; font-size: 30rpx; color: #999; }
.card-list { padding: 0 24rpx; }
.guide-card {
  background: #fff; border-radius: 24rpx; padding: 36rpx 32rpx;
  margin-bottom: 24rpx; display: flex; align-items: center;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.06);
}
.card-icon { font-size: 64rpx; margin-right: 24rpx; }
.card-info { flex: 1; }
.card-title { display: block; font-size: 34rpx; font-weight: bold; color: #333; margin-bottom: 8rpx; }
.card-desc { display: block; font-size: 28rpx; color: #999; line-height: 1.5; }
.card-status {
  font-size: 26rpx; padding: 8rpx 20rpx; border-radius: 20rpx;
  background: #f0f0f0; color: #999;
}
.card-status.active { background: #E8F5E9; color: #4CAF50; }
.privacy-bar {
  margin: 24rpx 24rpx 0; padding: 20rpx; text-align: center;
  background: #E8F5E9; border-radius: 16rpx; font-size: 28rpx; color: #2E7D32;
}
.bottom-area {
  position: fixed; bottom: 0; left: 0; right: 0;
  padding: 24rpx 48rpx 48rpx; background: #f5f5f5;
}
.next-btn {
  background: linear-gradient(135deg, #4CAF50, #66BB6A);
  color: #fff; text-align: center; padding: 28rpx 0;
  border-radius: 50rpx; font-size: 38rpx; font-weight: bold;
  box-shadow: 0 4rpx 16rpx rgba(76,175,80,0.3);
}
</style>
