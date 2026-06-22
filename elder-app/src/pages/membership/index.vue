<template>
  <view class="membership-page">
    <view class="plan-header">
      <text class="plan-header-title">选择适合您的方案</text>
      <text class="plan-header-desc">升级会员解锁更多健康监护功能</text>
    </view>

    <!-- 免费版 -->
    <view class="plan-card" :class="{ selected: selectedPlan === 0 }" @tap="selectPlan(0)">
      <view class="plan-badge free">免费版</view>
      <view class="plan-price">
        <text class="price-num">0</text>
        <text class="price-unit">元 / 永久</text>
      </view>
      <view class="plan-features">
        <view class="feature-row"><text class="feature-check">✓</text> 基础跌倒检测</view>
        <view class="feature-row"><text class="feature-check">✓</text> 一键呼救功能</view>
        <view class="feature-row"><text class="feature-check">✓</text> 每日健康简报</view>
        <view class="feature-row disabled"><text class="feature-x">✕</text> 情绪周报分析</view>
        <view class="feature-row disabled"><text class="feature-x">✕</text> 数据永久存档</view>
        <view class="feature-row disabled"><text class="feature-x">✕</text> 短信紧急预警</view>
      </view>
    </view>

    <!-- 月度会员 -->
    <view class="plan-card" :class="{ selected: selectedPlan === 1 }" @tap="selectPlan(1)">
      <view class="plan-badge monthly">月度会员</view>
      <view class="plan-price">
        <text class="price-num">19</text>
        <text class="price-unit">元 / 月</text>
      </view>
      <view class="plan-features">
        <view class="feature-row"><text class="feature-check">✓</text> 全部基础功能</view>
        <view class="feature-row"><text class="feature-check">✓</text> 情绪周报分析</view>
        <view class="feature-row"><text class="feature-check">✓</text> 短信紧急预警</view>
        <view class="feature-row"><text class="feature-check">✓</text> 30天数据存档</view>
        <view class="feature-row disabled"><text class="feature-x">✕</text> 数据永久存档</view>
      </view>
    </view>

    <!-- 年度会员（主推） -->
    <view class="plan-card recommended" :class="{ selected: selectedPlan === 2 }" @tap="selectPlan(2)">
      <view class="recommend-tag">推荐</view>
      <view class="plan-badge yearly">年度会员</view>
      <view class="plan-price">
        <text class="price-num">168</text>
        <text class="price-unit">元 / 年</text>
      </view>
      <view class="plan-save">比月付节省60元</view>
      <view class="plan-features">
        <view class="feature-row"><text class="feature-check">✓</text> 全部基础功能</view>
        <view class="feature-row"><text class="feature-check">✓</text> 情绪周报分析</view>
        <view class="feature-row"><text class="feature-check">✓</text> 短信紧急预警</view>
        <view class="feature-row"><text class="feature-check">✓</text> 数据永久存档</view>
        <view class="feature-row"><text class="feature-check">✓</text> 专属健康顾问</view>
      </view>
    </view>

    <!-- 支付按钮 -->
    <view class="pay-area">
      <view class="pay-btn" @tap="handlePay">
        {{ selectedPlan === 0 ? '当前使用免费版' : '微信支付 ¥' + payPrice }}
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      selectedPlan: 2,
      currentPlan: 0
    }
  },
  computed: {
    payPrice() {
      const prices = { 0: 0, 1: 19, 2: 168 }
      return prices[this.selectedPlan]
    }
  },
  methods: {
    selectPlan(plan) {
      this.selectedPlan = plan
    },
    handlePay() {
      if (this.selectedPlan === 0) {
        uni.showToast({ title: '您已是免费版用户', icon: 'none' })
        return
      }
      uni.showModal({
        title: '确认支付',
        content: `确定支付 ¥${this.payPrice} 开通${this.selectedPlan === 1 ? '月度' : '年度'}会员？`,
        success: (res) => {
          if (res.confirm) {
            uni.showToast({ title: '支付成功，会员已开通', icon: 'success' })
            setTimeout(() => {
              uni.navigateBack()
            }, 1500)
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.membership-page { min-height: 100vh; background: #f5f5f5; padding-bottom: 160rpx; }
.plan-header { text-align: center; padding: 48rpx 0 24rpx; }
.plan-header-title { display: block; font-size: 42rpx; font-weight: bold; color: #333; }
.plan-header-desc { display: block; font-size: 30rpx; color: #999; margin-top: 8rpx; }

.plan-card {
  background: #fff; border-radius: 24rpx; padding: 36rpx;
  margin: 20rpx 24rpx; border: 3rpx solid transparent;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.06); position: relative;
}
.plan-card.selected { border-color: #4CAF50; }
.plan-card.recommended { border-color: #FF9800; background: linear-gradient(180deg, #FFF8E1, #fff); }

.recommend-tag {
  position: absolute; top: -2rpx; right: 32rpx;
  background: #FF9800; color: #fff; padding: 6rpx 24rpx;
  border-radius: 0 0 12rpx 12rpx; font-size: 26rpx; font-weight: bold;
}

.plan-badge {
  display: inline-block; padding: 8rpx 24rpx; border-radius: 20rpx;
  font-size: 28rpx; font-weight: bold; margin-bottom: 16rpx;
}
.plan-badge.free { background: #E8F5E9; color: #2E7D32; }
.plan-badge.monthly { background: #E3F2FD; color: #1565C0; }
.plan-badge.yearly { background: #FFF3E0; color: #E65100; }

.plan-price { margin-bottom: 12rpx; }
.price-num { font-size: 56rpx; font-weight: bold; color: #333; }
.price-unit { font-size: 28rpx; color: #999; }

.plan-save {
  font-size: 28rpx; color: #FF9800; margin-bottom: 16rpx;
  background: #FFF8E1; display: inline-block; padding: 4rpx 16rpx;
  border-radius: 8rpx;
}

.plan-features { margin-top: 12rpx; }
.feature-row {
  font-size: 32rpx; color: #555; padding: 8rpx 0;
  display: flex; align-items: center;
}
.feature-row.disabled { color: #ccc; }
.feature-check { color: #4CAF50; margin-right: 12rpx; font-weight: bold; }
.feature-x { color: #ccc; margin-right: 12rpx; }

.pay-area {
  position: fixed; bottom: 0; left: 0; right: 0;
  padding: 24rpx 48rpx 48rpx; background: #fff;
  box-shadow: 0 -4rpx 16rpx rgba(0,0,0,0.06);
}
.pay-btn {
  background: linear-gradient(135deg, #4CAF50, #66BB6A);
  color: #fff; text-align: center; padding: 28rpx 0;
  border-radius: 50rpx; font-size: 38rpx; font-weight: bold;
}
</style>
