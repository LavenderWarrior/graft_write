<template>
  <view class="dialog-mask" v-if="visible" @touchmove.stop.prevent>
    <view class="dialog-box">
      <view class="dialog-icon">🆘</view>
      <view class="dialog-title">确认呼救</view>
      <view class="dialog-content">
        即将拨打子女、社区养老中心电话，是否确认呼救？
      </view>
      <view class="dialog-buttons">
        <view class="dialog-btn btn-cancel" @tap="onCancel">取消</view>
        <view class="dialog-btn btn-sos" @tap="onCall">立即呼叫</view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'SosConfirmDialog',
  props: {
    visible: { type: Boolean, default: false }
  },
  methods: {
    onCancel() { this.$emit('cancel') },
    onCall() { this.$emit('call') }
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
.dialog-icon { font-size: 100rpx; margin-bottom: 20rpx; }
.dialog-title {
  font-size: 40rpx; font-weight: bold; margin-bottom: 24rpx; color: #D32F2F;
}
.dialog-content {
  font-size: 34rpx; color: #555; line-height: 1.6; margin-bottom: 40rpx;
}
.dialog-buttons { display: flex; gap: 20rpx; }
.dialog-btn {
  flex: 1; text-align: center; padding: 26rpx 0;
  border-radius: 20rpx; font-size: 36rpx; font-weight: bold;
}
.btn-cancel { background: #f5f5f5; color: #999; }
.btn-sos { background: #D32F2F; color: #fff; }
</style>
