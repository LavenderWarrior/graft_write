<script>
export default {
  onLaunch() {
    console.log('App Launch - 银发健康监护老人端')
  },
  onShow() {
    console.log('App Show')
  },
  onHide() {
    console.log('App Hide')
  }
}
</script>

<style>
/* 全局样式 - 老人端大字体 */
page {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  font-size: 36rpx;
  color: #333333;
  background-color: #f5f5f5;
}

/* 通用大按钮样式 */
.btn-primary {
  background: linear-gradient(135deg, #4CAF50, #66BB6A);
  color: #ffffff;
  border-radius: 24rpx;
  font-size: 38rpx;
  font-weight: bold;
  padding: 28rpx 0;
  text-align: center;
  box-shadow: 0 4rpx 16rpx rgba(76, 175, 80, 0.3);
}

.btn-danger {
  background: linear-gradient(135deg, #F44336, #EF5350);
  color: #ffffff;
  border-radius: 24rpx;
  font-size: 38rpx;
  font-weight: bold;
  padding: 28rpx 0;
  text-align: center;
  box-shadow: 0 4rpx 16rpx rgba(244, 67, 54, 0.3);
}

.btn-secondary {
  background: #f0f0f0;
  color: #666666;
  border-radius: 24rpx;
  font-size: 36rpx;
  padding: 24rpx 0;
  text-align: center;
}

/* 卡片通用样式 */
.card {
  background: #ffffff;
  border-radius: 24rpx;
  padding: 32rpx;
  margin: 20rpx 24rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
}

/* 隐私提示条 */
.privacy-bar {
  background: #E8F5E9;
  padding: 16rpx 24rpx;
  font-size: 26rpx;
  color: #2E7D32;
  text-align: center;
  border-radius: 12rpx;
}
</style>
