<template>
  <view class="dialog-mask" v-if="visible" @touchmove.stop.prevent>
    <view class="dialog-box">
      <view class="dialog-icon">🎙️</view>
      <view class="dialog-title">情绪监测需要麦克风权限</view>
      <view class="dialog-content">
        麦克风采集日常语音分析情绪、夜间异响，音频本地加密存储，7天自动清除，不会上传网络。
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
  name: 'MicPermissionDialog',
  props: {
    visible: { type: Boolean, default: false }
  },
  methods: {
    onAllow() { this.$emit('allow') },
    onRefuse() { this.$emit('refuse') }
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
  margin-bottom: 36rpx;
}
.dialog-buttons { display: flex; gap: 20rpx; }
.dialog-btn {
  flex: 1; text-align: center; padding: 24rpx 0;
  border-radius: 20rpx; font-size: 34rpx; font-weight: bold;
}
.btn-refuse { background: #f5f5f5; color: #999; }
.btn-allow { background: #4CAF50; color: #fff; }
</style>
