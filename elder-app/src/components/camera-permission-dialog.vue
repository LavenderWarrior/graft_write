<template>
  <view class="dialog-mask" v-if="visible" @touchmove.stop.prevent>
    <view class="dialog-box">
      <view class="dialog-icon">📷</view>
      <view class="dialog-title">监护功能需要相机权限</view>
      <view class="dialog-content">
        本小程序仅通过手机后置摄像头识别您居家行走、跌倒、睡眠状态，<text class="highlight">所有画面仅本地处理，不上传云端</text>，您可随时在设置关闭权限。
      </view>
      <view class="no-remind-row" v-if="showNoRemind" @tap="toggleNoRemind">
        <view class="checkbox-mini" :class="{ checked: noRemind }">
          <text v-if="noRemind">✓</text>
        </view>
        <text class="no-remind-text">不再提醒</text>
      </view>
      <view class="dialog-buttons">
        <view class="dialog-btn btn-refuse" @tap="onRefuse">拒绝</view>
        <view class="dialog-btn btn-allow" @tap="onAllow">允许</view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'CameraPermissionDialog',
  props: {
    visible: { type: Boolean, default: false },
    showNoRemind: { type: Boolean, default: false }
  },
  data() {
    return { noRemind: false }
  },
  methods: {
    toggleNoRemind() { this.noRemind = !this.noRemind },
    onAllow() { this.$emit('allow') },
    onRefuse() { this.$emit('refuse', this.noRemind) }
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
  width: 620rpx; background: #fff; border-radius: 28rpx;
  padding: 48rpx 40rpx; box-sizing: border-box; text-align: center;
}
.dialog-icon { font-size: 80rpx; margin-bottom: 20rpx; }
.dialog-title {
  font-size: 38rpx; font-weight: bold; margin-bottom: 28rpx; color: #333;
}
.dialog-content {
  font-size: 32rpx; color: #666; line-height: 1.7; text-align: left;
  margin-bottom: 28rpx;
}
.highlight { color: #4CAF50; font-weight: bold; }
.no-remind-row {
  display: flex; align-items: center; justify-content: center;
  margin-bottom: 28rpx;
}
.checkbox-mini {
  width: 36rpx; height: 36rpx; border: 2rpx solid #ccc;
  border-radius: 6rpx; display: flex; align-items: center;
  justify-content: center; margin-right: 12rpx; font-size: 24rpx;
}
.checkbox-mini.checked { background: #4CAF50; border-color: #4CAF50; color: #fff; }
.no-remind-text { font-size: 28rpx; color: #999; }
.dialog-buttons { display: flex; gap: 20rpx; }
.dialog-btn {
  flex: 1; text-align: center; padding: 24rpx 0;
  border-radius: 20rpx; font-size: 34rpx; font-weight: bold;
}
.btn-refuse { background: #f5f5f5; color: #999; }
.btn-allow { background: #4CAF50; color: #fff; }
</style>
