<template>
  <view class="settings-page">
    <!-- 监护硬件总控区 -->
    <view class="card section-card">
      <view class="section-title">监护硬件总控</view>
      <view class="setting-item">
        <text class="setting-label">📷 相机权限</text>
        <switch :checked="cameraEnabled" @change="toggleCamera" color="#4CAF50" />
      </view>
      <view class="setting-item">
        <text class="setting-label">🎙️ 麦克风权限</text>
        <switch :checked="micEnabled" @change="toggleMic" color="#4CAF50" />
      </view>
      <view class="setting-item">
        <text class="setting-label">🔋 低功耗省电模式</text>
        <switch :checked="lowPowerMode" @change="toggleLowPower" color="#4CAF50" />
      </view>
      <view class="setting-item">
        <text class="setting-label">💡 屏幕常亮</text>
        <switch :checked="screenOn" @change="toggleScreen" color="#4CAF50" />
      </view>
    </view>

    <!-- 通知设置 -->
    <view class="card section-card">
      <view class="section-title">通知设置</view>
      <view class="setting-item">
        <text class="setting-label">🔔 本地提醒</text>
        <switch :checked="localNotify" @change="toggleLocalNotify" color="#4CAF50" />
      </view>
      <view class="setting-item">
        <text class="setting-label">📱 短信紧急通知</text>
        <switch :checked="smsNotify" @change="toggleSmsNotify" color="#4CAF50" />
      </view>
    </view>

    <!-- 隐私管理 -->
    <view class="card section-card">
      <view class="section-title">隐私管理</view>
      <view class="setting-item clickable" @tap="clearLocalData">
        <text class="setting-label">🗑️ 一键清除本地数据</text>
        <text class="setting-arrow">›</text>
      </view>
      <view class="setting-item clickable" @tap="viewPrivacy">
        <text class="setting-label">📄 查看完整隐私协议</text>
        <text class="setting-arrow">›</text>
      </view>
    </view>

    <!-- 账号管理 -->
    <view class="card section-card">
      <view class="section-title">账号管理</view>
      <view class="setting-item clickable" @tap="goBind">
        <text class="setting-label">👨‍👩‍👧 绑定子女/社区管理员</text>
        <text class="setting-arrow">›</text>
      </view>
      <view class="setting-item clickable" @tap="logout">
        <text class="setting-label">🚪 退出登录</text>
        <text class="setting-arrow">›</text>
      </view>
    </view>

    <!-- 会员服务入口 -->
    <view class="card section-card member-card" @tap="goMembership">
      <view class="member-info">
        <text class="member-icon">👑</text>
        <view class="member-text">
          <text class="member-title">{{ isVip ? '会员服务' : '升级会员' }}</text>
          <text class="member-desc">{{ isVip ? '已开通会员服务' : '解锁更多健康监护功能' }}</text>
        </view>
      </view>
      <text class="setting-arrow">›</text>
    </view>

    <!-- 隐私提示 -->
    <view class="privacy-bar">
      本地运算不上传画面语音，数据安全有保障
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      cameraEnabled: false,
      micEnabled: false,
      lowPowerMode: false,
      screenOn: false,
      localNotify: true,
      smsNotify: false,
      isVip: false
    }
  },
  methods: {
    toggleCamera(e) {
      this.cameraEnabled = e.detail.value
      uni.setStorageSync('cameraEnabled', this.cameraEnabled)
      if (!this.cameraEnabled) {
        uni.setStorageSync('monitorOn', false)
      }
    },
    toggleMic(e) {
      this.micEnabled = e.detail.value
      uni.setStorageSync('micEnabled', this.micEnabled)
    },
    toggleLowPower(e) {
      this.lowPowerMode = e.detail.value
      uni.setStorageSync('lowPowerMode', this.lowPowerMode)
    },
    toggleScreen(e) {
      this.screenOn = e.detail.value
      uni.setKeepScreenOn({ keepScreenOn: this.screenOn })
      uni.setStorageSync('screenOn', this.screenOn)
    },
    toggleLocalNotify(e) {
      this.localNotify = e.detail.value
    },
    toggleSmsNotify(e) {
      this.smsNotify = e.detail.value
    },
    clearLocalData() {
      uni.showModal({
        title: '确认清除',
        content: '将删除7天内音视频缓存数据，此操作不可恢复，确定清除？',
        confirmText: '确认清除',
        confirmColor: '#F44336',
        success: (res) => {
          if (res.confirm) {
            uni.showToast({ title: '本地数据已清除', icon: 'success' })
          }
        }
      })
    },
    viewPrivacy() {
      uni.showModal({
        title: '隐私协议',
        content: '视频、音频数据仅本机本地AI运算，原始素材不上传服务器。您可一键关闭相机/麦克风，随时终止监测。仅风险文字结论推送子女/社区，无画面、语音外传。',
        showCancel: false
      })
    },
    goBind() {
      uni.navigateTo({ url: '/pages/binding/index' })
    },
    goMembership() {
      uni.navigateTo({ url: '/pages/membership/index' })
    },
    logout() {
      uni.showModal({
        title: '退出登录',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.clearStorageSync()
            uni.reLaunch({ url: '/pages/welcome/index' })
          }
        }
      })
    }
  },
  onShow() {
    this.cameraEnabled = uni.getStorageSync('cameraEnabled') || false
    this.micEnabled = uni.getStorageSync('micEnabled') || false
    this.lowPowerMode = uni.getStorageSync('lowPowerMode') || false
    this.screenOn = uni.getStorageSync('screenOn') || false
  }
}
</script>

<style scoped>
.settings-page { min-height: 100vh; background: #f5f5f5; padding-bottom: 140rpx; }

.section-card { margin-top: 20rpx; }
.section-title {
  font-size: 34rpx; font-weight: bold; color: #333; margin-bottom: 16rpx;
}

.setting-item {
  display: flex; align-items: center; justify-content: space-between;
  padding: 24rpx 0; border-bottom: 1rpx solid #f5f5f5;
}
.setting-item:last-child { border-bottom: none; }
.setting-label { font-size: 34rpx; color: #333; }
.setting-arrow { font-size: 40rpx; color: #ccc; }
.clickable { cursor: pointer; }

.member-card {
  display: flex; align-items: center; justify-content: space-between;
  background: linear-gradient(135deg, #FFF8E1, #FFECB3);
  border: 2rpx solid #FFD54F;
}
.member-info { display: flex; align-items: center; }
.member-icon { font-size: 56rpx; margin-right: 20rpx; }
.member-title { display: block; font-size: 34rpx; font-weight: bold; color: #F57F17; }
.member-desc { display: block; font-size: 28rpx; color: #F9A825; margin-top: 4rpx; }

.privacy-bar {
  margin: 24rpx 24rpx; padding: 20rpx; text-align: center;
  background: #E8F5E9; border-radius: 16rpx; font-size: 28rpx; color: #2E7D32;
}
</style>
