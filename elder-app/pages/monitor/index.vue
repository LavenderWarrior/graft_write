<template>
  <view class="monitor-page">
    <!-- 全屏摄像头取景区 -->
    <view class="camera-area">
      <camera
        v-if="cameraReady"
        device-position="back"
        flash="off"
        class="camera-view"
      ></camera>
      <view v-else class="camera-placeholder">
        <text class="placeholder-text">摄像头加载中...</text>
      </view>
    </view>

    <!-- 夜间模式提示 -->
    <view class="night-mode-bar" v-if="nightMode">
      🌙 夜间省电监测模式
    </view>

    <!-- 侧边悬浮控件 -->
    <view class="side-controls">
      <view class="side-btn" :class="{ active: micOn }" @tap="toggleMic">
        <text class="side-icon">{{ micOn ? '🎙️' : '🔇' }}</text>
        <text class="side-label">{{ micOn ? '麦克风开' : '麦克风关' }}</text>
      </view>
      <view class="side-btn" :class="{ active: screenOn }" @tap="toggleScreen">
        <text class="side-icon">💡</text>
        <text class="side-label">{{ screenOn ? '常亮开' : '常亮关' }}</text>
      </view>
      <view class="side-btn" @tap="minimize">
        <text class="side-icon">📱</text>
        <text class="side-label">后台运行</text>
      </view>
    </view>

    <!-- 底部状态提示 -->
    <view class="bottom-bar">
      <view class="privacy-bar">
        正在监测行走、跌倒、夜间活动，画面仅本地处理
      </view>
      <view class="monitor-time">
        监护时长：{{ formatDuration(duration) }}
      </view>
    </view>

    <!-- 高风险告警弹窗 -->
    <risk-alert-dialog
      :visible="showAlert"
      :level="3"
      description="疑似发生跌倒，已同步通知子女+社区网格员"
      @cancel="dismissAlert"
      @sos="emergencyCall"
    />
  </view>
</template>

<script>
import RiskAlertDialog from '../../components/risk-alert-dialog.vue'

export default {
  components: { RiskAlertDialog },
  data() {
    return {
      cameraReady: true,
      micOn: true,
      screenOn: false,
      nightMode: false,
      showAlert: false,
      duration: 0,
      timer: null
    }
  },
  methods: {
    toggleMic() {
      this.micOn = !this.micOn
      uni.showToast({
        title: this.micOn ? '麦克风已开启' : '麦克风已关闭',
        icon: 'none'
      })
    },
    toggleScreen() {
      this.screenOn = !this.screenOn
      uni.setKeepScreenOn({ keepScreenOn: this.screenOn })
      uni.showToast({
        title: this.screenOn ? '屏幕常亮已开启' : '屏幕常亮已关闭',
        icon: 'none'
      })
    },
    minimize() {
      uni.navigateBack()
    },
    dismissAlert() {
      this.showAlert = false
      uni.showToast({ title: '告警已取消', icon: 'none' })
    },
    emergencyCall() {
      this.showAlert = false
      uni.makePhoneCall({ phoneNumber: '120' })
    },
    formatDuration(seconds) {
      const h = Math.floor(seconds / 3600)
      const m = Math.floor((seconds % 3600) / 60)
      const s = seconds % 60
      return `${h > 0 ? h + '时' : ''}${m}分${s}秒`
    },
    startTimer() {
      this.timer = setInterval(() => { this.duration++ }, 1000)
    }
  },
  onLoad() {
    this.startTimer()
    uni.setKeepScreenOn({ keepScreenOn: false })
  },
  onUnload() {
    if (this.timer) clearInterval(this.timer)
  }
}
</script>

<style scoped>
.monitor-page {
  height: 100vh; background: #000; position: relative; overflow: hidden;
}
.camera-area { width: 100%; height: 100%; }
.camera-view { width: 100%; height: 100%; }
.camera-placeholder {
  width: 100%; height: 100%; display: flex;
  align-items: center; justify-content: center; background: #1a1a1a;
}
.placeholder-text { color: #666; font-size: 32rpx; }

.night-mode-bar {
  position: absolute; top: 100rpx; left: 50%; transform: translateX(-50%);
  background: rgba(0,0,0,0.7); color: #FFD54F; padding: 12rpx 32rpx;
  border-radius: 32rpx; font-size: 28rpx; z-index: 10;
}

.side-controls {
  position: absolute; right: 20rpx; top: 50%; transform: translateY(-50%);
  display: flex; flex-direction: column; gap: 24rpx; z-index: 10;
}
.side-btn {
  background: rgba(0,0,0,0.5); border-radius: 20rpx;
  padding: 20rpx 16rpx; text-align: center; min-width: 120rpx;
}
.side-btn.active { background: rgba(76,175,80,0.7); }
.side-icon { display: block; font-size: 48rpx; margin-bottom: 8rpx; }
.side-label { display: block; font-size: 22rpx; color: #fff; }

.bottom-bar {
  position: absolute; bottom: 0; left: 0; right: 0;
  padding: 20rpx 24rpx 48rpx; z-index: 10;
}
.bottom-bar .privacy-bar {
  background: rgba(46,125,50,0.8); color: #C8E6C9;
  padding: 16rpx 24rpx; border-radius: 12rpx;
  font-size: 26rpx; text-align: center; margin-bottom: 12rpx;
}
.monitor-time {
  text-align: center; color: rgba(255,255,255,0.6); font-size: 26rpx;
}
</style>
