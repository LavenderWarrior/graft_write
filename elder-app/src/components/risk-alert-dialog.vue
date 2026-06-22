<template>
  <view class="dialog-mask" v-if="visible" @touchmove.stop.prevent>
    <!-- 低风险黄色弹窗 -->
    <view class="dialog-box level-low" v-if="level === 1">
      <view class="alert-icon">⚠️</view>
      <view class="alert-title">作息轻微异常</view>
      <view class="alert-desc">{{ description || '检测到作息轻微异常，仅本地提醒' }}</view>
      <view class="alert-note">本次提醒仅限本地，未发送短信推送</view>
      <view class="dialog-buttons">
        <view class="dialog-btn btn-yellow" @tap="onConfirm">我知道了</view>
      </view>
    </view>

    <!-- 中风险橙色弹窗 -->
    <view class="dialog-box level-mid" v-if="level === 2">
      <view class="alert-icon">🔶</view>
      <view class="alert-title">情绪/行为异常提醒</view>
      <view class="alert-desc">{{ description || '检测到情绪低落或长时间静坐' }}</view>
      <view class="alert-note">已推送子女微信消息通知</view>
      <view class="dialog-buttons">
        <view class="dialog-btn btn-orange" @tap="onConfirm">我知道了</view>
      </view>
    </view>

    <!-- 高风险红色全屏弹窗 -->
    <view class="dialog-box-full level-high" v-if="level === 3">
      <view class="alert-icon-large">🚨</view>
      <view class="alert-title-large">紧急告警</view>
      <view class="alert-desc-large">{{ description || '疑似发生跌倒' }}</view>
      <view class="alert-note-large">已同步通知子女 + 社区网格员</view>
      <view class="dialog-buttons-vertical">
        <view class="dialog-btn btn-white-outline" @tap="onCancel">我没事（取消告警）</view>
        <view class="dialog-btn btn-red-sos" @tap="onSOS">一键呼救</view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'RiskAlertDialog',
  props: {
    visible: { type: Boolean, default: false },
    level: { type: Number, default: 1 },
    description: { type: String, default: '' }
  },
  methods: {
    onConfirm() { this.$emit('confirm') },
    onCancel() { this.$emit('cancel') },
    onSOS() { this.$emit('sos') }
  }
}
</script>

<style scoped>
.dialog-mask {
  position: fixed; top: 0; left: 0; right: 0; bottom: 0;
  display: flex; align-items: center; justify-content: center; z-index: 999;
}
/* 低/中风险小弹窗 */
.dialog-box {
  width: 620rpx; border-radius: 28rpx;
  padding: 48rpx 40rpx; text-align: center;
}
.dialog-box.level-low {
  background: #FFF8E1; border: 4rpx solid #FFC107;
}
.dialog-box.level-mid {
  background: #FFF3E0; border: 4rpx solid #FF9800;
}
.dialog-mask { background: rgba(0,0,0,0.7); }
.alert-icon { font-size: 80rpx; margin-bottom: 16rpx; }
.alert-title { font-size: 38rpx; font-weight: bold; margin-bottom: 20rpx; color: #333; }
.alert-desc { font-size: 32rpx; color: #555; line-height: 1.6; margin-bottom: 16rpx; }
.alert-note { font-size: 28rpx; color: #999; margin-bottom: 32rpx; }
.dialog-buttons { display: flex; gap: 20rpx; }
.dialog-btn {
  flex: 1; text-align: center; padding: 24rpx 0;
  border-radius: 20rpx; font-size: 34rpx; font-weight: bold;
}
.btn-yellow { background: #FFC107; color: #333; }
.btn-orange { background: #FF9800; color: #fff; }

/* 高风险全屏 */
.dialog-box-full {
  width: 100vw; height: 100vh; background: linear-gradient(180deg, #D32F2F, #B71C1C);
  display: flex; flex-direction: column; align-items: center;
  justify-content: center; padding: 60rpx;
}
.alert-icon-large { font-size: 140rpx; margin-bottom: 40rpx; }
.alert-title-large { font-size: 52rpx; font-weight: bold; color: #fff; margin-bottom: 32rpx; }
.alert-desc-large { font-size: 40rpx; color: #FFCDD2; margin-bottom: 24rpx; text-align: center; line-height: 1.6; }
.alert-note-large { font-size: 32rpx; color: #EF9A9A; margin-bottom: 60rpx; }
.dialog-buttons-vertical { width: 100%; display: flex; flex-direction: column; gap: 28rpx; padding: 0 40rpx; }
.btn-white-outline {
  background: transparent; border: 3rpx solid #fff; color: #fff;
  border-radius: 24rpx; padding: 28rpx 0; text-align: center;
  font-size: 36rpx; font-weight: bold;
}
.btn-red-sos {
  background: #fff; color: #D32F2F; border-radius: 24rpx;
  padding: 32rpx 0; text-align: center; font-size: 40rpx; font-weight: bold;
  box-shadow: 0 8rpx 32rpx rgba(0,0,0,0.3);
}
</style>
