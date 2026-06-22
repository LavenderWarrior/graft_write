<template>
  <view class="welcome-page">
    <view class="welcome-bg">
      <view class="logo-area">
        <view class="logo-icon">🏥</view>
        <text class="logo-text">银发健康监护</text>
      </view>
      <view class="slogan">不用穿戴设备，手机守护居家安全</view>
      <view class="features">
        <view class="feature-tag">跌倒检测</view>
        <view class="feature-tag">睡眠监测</view>
        <view class="feature-tag">情绪分析</view>
      </view>
    </view>
    <view class="bottom-area">
      <view class="enter-btn" @tap="handleEnter">立即进入</view>
    </view>

    <privacy-dialog
      :visible="showPrivacy"
      @confirm="onPrivacyConfirm"
      @exit="onPrivacyExit"
    />
  </view>
</template>

<script>
import PrivacyDialog from '../../components/privacy-dialog.vue'

export default {
  components: { PrivacyDialog },
  data() {
    return {
      showPrivacy: false
    }
  },
  methods: {
    handleEnter() {
      this.showPrivacy = true
    },
    onPrivacyConfirm() {
      this.showPrivacy = false
      uni.setStorageSync('privacyAgreed', true)
      uni.navigateTo({ url: '/pages/permission-guide/index' })
    },
    onPrivacyExit() {
      this.showPrivacy = false
      uni.showToast({ title: '您已退出', icon: 'none' })
    }
  },
  onLoad() {
    const agreed = uni.getStorageSync('privacyAgreed')
    if (agreed) {
      uni.switchTab({ url: '/pages/home/index' })
    }
  }
}
</script>

<style scoped>
.welcome-page {
  height: 100vh; display: flex; flex-direction: column;
  background: linear-gradient(180deg, #4CAF50 0%, #2E7D32 60%, #1B5E20 100%);
}
.welcome-bg {
  flex: 1; display: flex; flex-direction: column;
  align-items: center; justify-content: center; padding: 60rpx;
}
.logo-area { text-align: center; margin-bottom: 40rpx; }
.logo-icon { font-size: 140rpx; margin-bottom: 20rpx; }
.logo-text {
  font-size: 56rpx; font-weight: bold; color: #ffffff;
  letter-spacing: 4rpx;
}
.slogan {
  font-size: 36rpx; color: #C8E6C9; text-align: center;
  margin-bottom: 48rpx; line-height: 1.5;
}
.features { display: flex; gap: 20rpx; flex-wrap: wrap; justify-content: center; }
.feature-tag {
  background: rgba(255,255,255,0.2); color: #fff;
  padding: 12rpx 32rpx; border-radius: 40rpx; font-size: 30rpx;
}
.bottom-area { padding: 48rpx 60rpx 80rpx; }
.enter-btn {
  background: #ffffff; color: #2E7D32; text-align: center;
  padding: 32rpx 0; border-radius: 50rpx; font-size: 40rpx;
  font-weight: bold; box-shadow: 0 8rpx 32rpx rgba(0,0,0,0.2);
}
</style>
