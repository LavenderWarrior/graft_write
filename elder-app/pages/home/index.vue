<template>
  <view class="home-page">
    <!-- 顶部状态栏 -->
    <view class="status-bar" :class="{ active: monitorOn }">
      <view class="status-tag">
        <view class="status-dot" :class="{ on: monitorOn }"></view>
        <text class="status-text">{{ monitorOn ? '监护开启中' : '监护已关闭' }}</text>
      </view>
    </view>

    <!-- 今日简易数据 -->
    <view class="today-summary card">
      <view class="summary-title">今日概况</view>
      <view class="summary-grid">
        <view class="summary-item">
          <text class="summary-value">{{ todayData.activityMinutes || 0 }}</text>
          <text class="summary-unit">分钟</text>
          <text class="summary-label">活动时长</text>
        </view>
        <view class="summary-divider"></view>
        <view class="summary-item">
          <text class="summary-value">{{ todayData.sleepScore || '--' }}</text>
          <text class="summary-unit">分</text>
          <text class="summary-label">睡眠评分</text>
        </view>
        <view class="summary-divider"></view>
        <view class="summary-item">
          <text class="summary-value emotion" :class="'emotion-' + todayData.emotionStatus">
            {{ emotionText }}
          </text>
          <text class="summary-label">情绪状态</text>
        </view>
      </view>
    </view>

    <!-- 核心功能区 -->
    <view class="function-grid">
      <view class="func-btn monitor-btn" :class="{ 'monitor-off': !monitorOn }" @tap="toggleMonitor">
        <view class="func-icon">{{ monitorOn ? '🛡️' : '⏸️' }}</view>
        <text class="func-text">{{ monitorOn ? '关闭监护' : '开启监护' }}</text>
      </view>

      <view class="func-btn sos-btn" @tap="handleSOS">
        <view class="func-icon">🆘</view>
        <text class="func-text">紧急一键呼救</text>
      </view>

      <view class="func-btn report-btn" @tap="goReport">
        <view class="func-icon">📊</view>
        <text class="func-text">查看健康简报</text>
      </view>

      <view class="func-btn bind-btn" @tap="goBind">
        <view class="func-icon">👨‍👩‍👧</view>
        <text class="func-text">绑定子女/社区</text>
      </view>
    </view>

    <!-- 隐私提示条 -->
    <view class="privacy-bar">
      本地运算不上传画面语音，数据安全有保障
    </view>

    <!-- 弹窗 -->
    <monitor-stop-dialog
      :visible="showStopDialog"
      @continue="showStopDialog = false"
      @close="confirmStopMonitor"
    />
    <sos-confirm-dialog
      :visible="showSOSDialog"
      @cancel="showSOSDialog = false"
      @call="confirmSOS"
    />
    <risk-alert-dialog
      :visible="showAlertDialog"
      :level="alertLevel"
      :description="alertDesc"
      @confirm="showAlertDialog = false"
      @cancel="cancelAlert"
      @sos="emergencySOS"
    />
  </view>
</template>

<script>
import MonitorStopDialog from '../../components/monitor-stop-dialog.vue'
import SosConfirmDialog from '../../components/sos-confirm-dialog.vue'
import RiskAlertDialog from '../../components/risk-alert-dialog.vue'
import api from '../../common/api.js'

export default {
  components: { MonitorStopDialog, SosConfirmDialog, RiskAlertDialog },
  data() {
    return {
      monitorOn: false,
      showStopDialog: false,
      showSOSDialog: false,
      showAlertDialog: false,
      alertLevel: 1,
      alertDesc: '',
      todayData: {
        activityMinutes: 0,
        sleepScore: 0,
        emotionStatus: 1
      },
      userId: null
    }
  },
  computed: {
    emotionText() {
      const map = { 1: '平稳', 2: '轻微低落', 3: '消极倾向' }
      return map[this.todayData.emotionStatus] || '平稳'
    }
  },
  methods: {
    toggleMonitor() {
      if (this.monitorOn) {
        this.showStopDialog = true
      } else {
        this.startMonitor()
      }
    },
    startMonitor() {
      const cameraEnabled = uni.getStorageSync('cameraEnabled')
      if (!cameraEnabled) {
        uni.showModal({
          title: '需要相机权限',
          content: '请先开启相机权限以启动监护功能',
          confirmText: '去设置',
          success: (res) => {
            if (res.confirm) {
              uni.switchTab({ url: '/pages/settings/index' })
            }
          }
        })
        return
      }
      this.monitorOn = true
      uni.setStorageSync('monitorOn', true)
      uni.navigateTo({ url: '/pages/monitor/index' })
    },
    confirmStopMonitor() {
      this.showStopDialog = false
      this.monitorOn = false
      uni.setStorageSync('monitorOn', false)
      uni.showToast({ title: '监护已关闭', icon: 'none' })
    },
    handleSOS() {
      this.showSOSDialog = true
    },
    confirmSOS() {
      this.showSOSDialog = false
      uni.makePhoneCall({
        phoneNumber: '120',
        fail: () => {
          uni.showToast({ title: '呼叫失败，请手动拨打', icon: 'none' })
        }
      })
    },
    cancelAlert() {
      this.showAlertDialog = false
      uni.showToast({ title: '告警已取消', icon: 'none' })
    },
    emergencySOS() {
      this.showAlertDialog = false
      this.confirmSOS()
    },
    goReport() {
      uni.switchTab({ url: '/pages/health-report/index' })
    },
    goBind() {
      uni.navigateTo({ url: '/pages/binding/index' })
    },
    loadTodayData() {
      const userId = uni.getStorageSync('userId')
      if (!userId) return
      api.getTodayReport(userId).then(data => {
        if (data) {
          this.todayData = data
        }
      }).catch(() => {})
    }
  },
  onShow() {
    this.monitorOn = uni.getStorageSync('monitorOn') || false
    this.loadTodayData()
  },
  onPullDownRefresh() {
    this.loadTodayData()
    setTimeout(() => { uni.stopPullDownRefresh() }, 1000)
  }
}
</script>

<style scoped>
.home-page { min-height: 100vh; background: #f5f5f5; padding-bottom: 140rpx; }

.status-bar {
  background: #E8E8E8; padding: 24rpx 32rpx;
  display: flex; align-items: center;
}
.status-bar.active { background: #E8F5E9; }
.status-tag { display: flex; align-items: center; }
.status-dot {
  width: 20rpx; height: 20rpx; border-radius: 50%;
  background: #ccc; margin-right: 12rpx;
}
.status-dot.on { background: #4CAF50; animation: pulse 2s infinite; }
@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}
.status-text { font-size: 32rpx; font-weight: bold; color: #333; }

.today-summary { margin-top: 20rpx; }
.summary-title { font-size: 34rpx; font-weight: bold; color: #333; margin-bottom: 24rpx; }
.summary-grid { display: flex; align-items: center; }
.summary-item { flex: 1; text-align: center; }
.summary-value { display: block; font-size: 48rpx; font-weight: bold; color: #4CAF50; }
.summary-unit { font-size: 26rpx; color: #999; }
.summary-label { display: block; font-size: 28rpx; color: #999; margin-top: 8rpx; }
.summary-divider { width: 2rpx; height: 60rpx; background: #eee; }
.emotion { font-size: 36rpx !important; }
.emotion-1 { color: #4CAF50 !important; }
.emotion-2 { color: #FF9800 !important; }
.emotion-3 { color: #F44336 !important; }

.function-grid {
  display: grid; grid-template-columns: 1fr 1fr;
  gap: 20rpx; padding: 20rpx 24rpx;
}
.func-btn {
  background: #fff; border-radius: 28rpx; padding: 36rpx 24rpx;
  text-align: center; box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.06);
  display: flex; flex-direction: column; align-items: center;
}
.func-icon { font-size: 72rpx; margin-bottom: 16rpx; }
.func-text { font-size: 34rpx; font-weight: bold; color: #333; }

.monitor-btn { background: linear-gradient(135deg, #4CAF50, #66BB6A); }
.monitor-btn .func-text { color: #fff; }
.monitor-btn.monitor-off { background: linear-gradient(135deg, #78909C, #90A4AE); }

.sos-btn { background: linear-gradient(135deg, #F44336, #EF5350); }
.sos-btn .func-text { color: #fff; }

.report-btn { border: 2rpx solid #E3F2FD; }
.bind-btn { border: 2rpx solid #FFF3E0; }

.privacy-bar {
  margin: 20rpx 24rpx; padding: 20rpx; text-align: center;
  background: #E8F5E9; border-radius: 16rpx; font-size: 28rpx; color: #2E7D32;
}
</style>
