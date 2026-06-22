<template>
  <view class="login-page">
    <view class="login-bg">
      <view class="logo-area">
        <view class="logo-icon">👨‍👩‍👧</view>
        <text class="logo-text">子女监护端</text>
        <text class="logo-subtitle">远程守护家中老人健康</text>
      </view>
    </view>

    <view class="login-area">
      <view class="login-btn wechat" @tap="wechatLogin">
        <text class="btn-icon">💬</text>
        <text class="btn-text">微信授权登录</text>
      </view>

      <view class="scan-entry" @tap="scanBind">
        <text class="scan-text">扫码绑定家中老人设备</text>
      </view>

      <view class="login-tips">
        登录后将接收老人风险推送通知
      </view>
    </view>

    <!-- 绑定权限弹窗 -->
    <view class="dialog-mask" v-if="showBindDialog" @touchmove.stop.prevent>
      <view class="dialog-box">
        <view class="dialog-icon">🔔</view>
        <view class="dialog-title">绑定权限申请</view>
        <view class="dialog-content">允许接收老人风险推送通知？</view>
        <view class="dialog-buttons">
          <view class="dialog-btn btn-cancel" @tap="showBindDialog = false">稍后设置</view>
          <view class="dialog-btn btn-confirm" @tap="allowNotify">允许</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      showBindDialog: false
    }
  },
  methods: {
    wechatLogin() {
      uni.setStorageSync('familyUserId', 'demo_family_001')
      uni.setStorageSync('familyLoggedIn', true)
      this.showBindDialog = true
    },
    allowNotify() {
      this.showBindDialog = false
      uni.setStorageSync('notifyAllowed', true)
      uni.switchTab({ url: '/pages/home/index' })
    },
    scanBind() {
      uni.scanCode({
        success: (res) => {
          uni.showToast({ title: '绑定成功', icon: 'success' })
        },
        fail: () => {
          uni.showToast({ title: '请使用微信扫码', icon: 'none' })
        }
      })
    }
  },
  onLoad() {
    if (uni.getStorageSync('familyLoggedIn')) {
      uni.switchTab({ url: '/pages/home/index' })
    }
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh; display: flex; flex-direction: column;
  background: linear-gradient(180deg, #2196F3, #1565C0);
}
.login-bg {
  flex: 1; display: flex; align-items: center; justify-content: center;
}
.logo-area { text-align: center; }
.logo-icon { font-size: 120rpx; margin-bottom: 20rpx; }
.logo-text { display: block; font-size: 48rpx; font-weight: bold; color: #fff; }
.logo-subtitle { display: block; font-size: 28rpx; color: #BBDEFB; margin-top: 8rpx; }

.login-area { padding: 48rpx; }
.login-btn {
  display: flex; align-items: center; justify-content: center;
  padding: 28rpx 0; border-radius: 50rpx; font-size: 34rpx;
  font-weight: bold; margin-bottom: 24rpx;
}
.login-btn.wechat { background: #07C160; color: #fff; }
.btn-icon { font-size: 40rpx; margin-right: 12rpx; }
.btn-text { color: #fff; }

.scan-entry { text-align: center; padding: 20rpx 0; }
.scan-text { font-size: 30rpx; color: #BBDEFB; text-decoration: underline; }
.login-tips { text-align: center; font-size: 26rpx; color: #90CAF9; margin-top: 20rpx; }

.dialog-mask {
  position: fixed; top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.6); display: flex;
  align-items: center; justify-content: center; z-index: 999;
}
.dialog-box {
  width: 580rpx; background: #fff; border-radius: 24rpx;
  padding: 44rpx 36rpx; text-align: center;
}
.dialog-icon { font-size: 72rpx; margin-bottom: 16rpx; }
.dialog-title { font-size: 36rpx; font-weight: bold; color: #333; margin-bottom: 16rpx; }
.dialog-content { font-size: 30rpx; color: #666; margin-bottom: 32rpx; }
.dialog-buttons { display: flex; gap: 16rpx; }
.dialog-btn {
  flex: 1; padding: 22rpx 0; border-radius: 16rpx;
  font-size: 32rpx; font-weight: bold; text-align: center;
}
.btn-cancel { background: #f0f0f0; color: #999; }
.btn-confirm { background: #2196F3; color: #fff; }
</style>
