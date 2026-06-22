<template>
  <view class="dialog-mask" v-if="visible" @touchmove.stop.prevent>
    <view class="dialog-box">
      <view class="dialog-icon">⏸️</view>
      <view class="dialog-title">确认关闭监护</view>
      <view class="dialog-content">
        关闭后摄像头、麦克风将全部停止采集，跌倒、情绪监测全部暂停，确定关闭？
      </view>
      <view class="dialog-buttons">
        <view class="dialog-btn btn-continue" @tap="onContinue">继续监护</view>
        <view class="dialog-btn btn-close" @tap="onClose">确认关闭</view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'MonitorStopDialog',
  props: {
    visible: { type: Boolean, default: false }
  },
  methods: {
    onContinue() { this.$emit('continue') },
    onClose() { this.$emit('close') }
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
  width: 600rpx; background: #fff; border-radius: 28rpx;
  padding: 48rpx 40rpx; text-align: center;
}
.dialog-icon { font-size: 80rpx; margin-bottom: 20rpx; }
.dialog-title {
  font-size: 38rpx; font-weight: bold; margin-bottom: 24rpx; color: #333;
}
.dialog-content {
  font-size: 32rpx; color: #666; line-height: 1.7; margin-bottom: 36rpx;
}
.dialog-buttons { display: flex; gap: 20rpx; }
.dialog-btn {
  flex: 1; text-align: center; padding: 24rpx 0;
  border-radius: 20rpx; font-size: 34rpx; font-weight: bold;
}
.btn-continue { background: #4CAF50; color: #fff; }
.btn-close { background: #f5f5f5; color: #F44336; }
</style>
