<template>
  <view class="dialog-mask" v-if="visible" @touchmove.stop.prevent>
    <view class="dialog-box">
      <view class="dialog-title">隐私安全告知书</view>
      <view class="dialog-content">
        <view class="privacy-item">
          <text class="item-num">1.</text>
          <text>视频、音频数据仅本机本地AI运算，原始素材不上传服务器；</text>
        </view>
        <view class="privacy-item">
          <text class="item-num">2.</text>
          <text>您可一键关闭相机/麦克风，随时终止监测；</text>
        </view>
        <view class="privacy-item">
          <text class="item-num">3.</text>
          <text>仅风险文字结论推送子女/社区，无画面、语音外传。</text>
        </view>
      </view>
      <view class="checkbox-row" @tap="toggleAgree">
        <view class="checkbox" :class="{ checked: agreed }">
          <text v-if="agreed">✓</text>
        </view>
        <text class="checkbox-label">我已阅读并同意隐私协议</text>
      </view>
      <view class="dialog-buttons">
        <view class="dialog-btn btn-cancel" @tap="onExit">退出小程序</view>
        <view class="dialog-btn btn-confirm" :class="{ disabled: !agreed }" @tap="onConfirm">确认进入</view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'PrivacyDialog',
  props: {
    visible: { type: Boolean, default: false }
  },
  data() {
    return { agreed: false }
  },
  methods: {
    toggleAgree() {
      this.agreed = !this.agreed
    },
    onConfirm() {
      if (!this.agreed) {
        uni.showToast({ title: '请先勾选同意隐私协议', icon: 'none' })
        return
      }
      this.$emit('confirm')
    },
    onExit() {
      this.$emit('exit')
    }
  }
}
</script>

<style scoped>
.dialog-mask {
  position: fixed; top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.6); display: flex;
  align-items: center; justify-content: center; z-index: 999;
}
.dialog-box {
  width: 640rpx; background: #fff; border-radius: 28rpx;
  padding: 48rpx 40rpx; box-sizing: border-box;
}
.dialog-title {
  font-size: 40rpx; font-weight: bold; text-align: center;
  margin-bottom: 36rpx; color: #333;
}
.dialog-content { margin-bottom: 32rpx; }
.privacy-item {
  display: flex; margin-bottom: 20rpx; font-size: 32rpx;
  line-height: 1.6; color: #555;
}
.item-num { color: #4CAF50; font-weight: bold; margin-right: 8rpx; }
.checkbox-row {
  display: flex; align-items: center; margin-bottom: 36rpx; padding: 0 8rpx;
}
.checkbox {
  width: 44rpx; height: 44rpx; border: 3rpx solid #ccc;
  border-radius: 8rpx; display: flex; align-items: center;
  justify-content: center; margin-right: 16rpx; font-size: 28rpx;
}
.checkbox.checked { background: #4CAF50; border-color: #4CAF50; color: #fff; }
.checkbox-label { font-size: 30rpx; color: #666; }
.dialog-buttons { display: flex; gap: 20rpx; }
.dialog-btn {
  flex: 1; text-align: center; padding: 24rpx 0;
  border-radius: 20rpx; font-size: 34rpx; font-weight: bold;
}
.btn-cancel { background: #f5f5f5; color: #999; }
.btn-confirm { background: #4CAF50; color: #fff; }
.btn-confirm.disabled { opacity: 0.5; }
</style>
