<template>
  <view class="health-page">
    <!-- 周期切换 -->
    <view class="period-bar">
      <view class="period-item" :class="{ active: period === 'day' }" @tap="switchPeriod('day')">日</view>
      <view class="period-item" :class="{ active: period === 'week' }" @tap="switchPeriod('week')">周</view>
      <view class="period-item" :class="{ active: period === 'month' }" @tap="switchPeriod('month')">月</view>
    </view>

    <!-- 活动量折线图区域 -->
    <view class="card chart-card">
      <view class="chart-title">📊 每日活动量趋势</view>
      <view class="chart-area">
        <view class="bar-chart">
          <view v-for="(item, idx) in activityData" :key="idx" class="bar-col">
            <view class="bar" :style="{ height: (item.value / maxActivity * 200) + 'rpx' }">
              <text class="bar-val">{{ item.value }}</text>
            </view>
            <text class="bar-label">{{ item.label }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 睡眠时长图表 -->
    <view class="card chart-card">
      <view class="chart-title">😴 睡眠评分趋势</view>
      <view class="chart-area">
        <view class="bar-chart">
          <view v-for="(item, idx) in sleepData" :key="idx" class="bar-col">
            <view class="bar sleep-bar" :style="{ height: (item.value / 100 * 200) + 'rpx' }">
              <text class="bar-val">{{ item.value }}</text>
            </view>
            <text class="bar-label">{{ item.label }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 情绪波动趋势 -->
    <view class="card chart-card">
      <view class="chart-title">💭 情绪波动趋势</view>
      <view class="emotion-timeline">
        <view v-for="(item, idx) in emotionData" :key="idx" class="emotion-item">
          <text class="emotion-day">{{ item.label }}</text>
          <view class="emotion-badge" :class="'emotion-' + item.value">
            {{ emotionText(item.value) }}
          </view>
        </view>
      </view>
    </view>

    <!-- 系统建议 -->
    <view class="card advice-card">
      <view class="advice-title">💡 系统建议</view>
      <view v-for="(advice, idx) in adviceList" :key="idx" class="advice-item">
        <text class="advice-dot">•</text>
        <text class="advice-text">{{ advice }}</text>
      </view>
    </view>
  </view>
</template>

<script>
import api from '../../common/api.js'

export default {
  data() {
    return {
      period: 'week',
      activityData: [
        { label: '周一', value: 45 }, { label: '周二', value: 60 },
        { label: '周三', value: 30 }, { label: '周四', value: 55 },
        { label: '周五', value: 40 }, { label: '周六', value: 70 },
        { label: '周日', value: 50 }
      ],
      sleepData: [
        { label: '周一', value: 75 }, { label: '周二', value: 80 },
        { label: '周三', value: 65 }, { label: '周四', value: 70 },
        { label: '周五', value: 85 }, { label: '周六', value: 60 },
        { label: '周日', value: 78 }
      ],
      emotionData: [
        { label: '周一', value: 1 }, { label: '周二', value: 1 },
        { label: '周三', value: 2 }, { label: '周四', value: 1 },
        { label: '周五', value: 1 }, { label: '周六', value: 2 },
        { label: '周日', value: 1 }
      ],
      adviceList: [
        '本周活动量整体良好，建议保持每日外出散步30分钟',
        '周三、周六睡眠评分偏低，建议关注睡眠环境',
        '情绪总体平稳，周三、周六有轻微低落，建议多与老人视频通话'
      ]
    }
  },
  computed: {
    maxActivity() {
      return Math.max(...this.activityData.map(d => d.value), 1)
    }
  },
  methods: {
    switchPeriod(p) {
      this.period = p
    },
    emotionText(val) {
      const map = { 1: '平稳', 2: '低落', 3: '消极' }
      return map[val] || '平稳'
    }
  }
}
</script>

<style scoped>
.health-page { min-height: 100vh; background: #f5f5f5; padding-bottom: 140rpx; }

.period-bar {
  display: flex; background: #fff; padding: 16rpx 24rpx; gap: 12rpx;
}
.period-item {
  flex: 1; text-align: center; padding: 16rpx 0; border-radius: 12rpx;
  font-size: 30rpx; color: #999; background: #f5f5f5;
}
.period-item.active { background: #2196F3; color: #fff; font-weight: bold; }

.chart-card { margin-top: 16rpx; }
.chart-title { font-size: 32rpx; font-weight: bold; color: #333; margin-bottom: 20rpx; }
.chart-area { overflow-x: auto; }

.bar-chart { display: flex; align-items: flex-end; gap: 12rpx; min-height: 260rpx; padding-top: 40rpx; }
.bar-col { flex: 1; display: flex; flex-direction: column; align-items: center; }
.bar {
  width: 48rpx; background: linear-gradient(180deg, #4CAF50, #81C784);
  border-radius: 8rpx 8rpx 0 0; min-height: 20rpx; position: relative;
  display: flex; align-items: flex-start; justify-content: center;
}
.sleep-bar { background: linear-gradient(180deg, #7C4DFF, #B388FF); }
.bar-val {
  position: absolute; top: -36rpx; font-size: 22rpx; color: #666;
  white-space: nowrap;
}
.bar-label { font-size: 22rpx; color: #999; margin-top: 8rpx; }

.emotion-timeline { display: flex; gap: 12rpx; overflow-x: auto; }
.emotion-item { flex: 1; text-align: center; min-width: 80rpx; }
.emotion-day { display: block; font-size: 24rpx; color: #999; margin-bottom: 8rpx; }
.emotion-badge {
  padding: 8rpx 0; border-radius: 12rpx; font-size: 24rpx; font-weight: bold;
}
.emotion-badge.emotion-1 { background: #E8F5E9; color: #2E7D32; }
.emotion-badge.emotion-2 { background: #FFF3E0; color: #E65100; }
.emotion-badge.emotion-3 { background: #FFEBEE; color: #C62828; }

.advice-card { margin-top: 16rpx; }
.advice-title { font-size: 32rpx; font-weight: bold; color: #333; margin-bottom: 16rpx; }
.advice-item { display: flex; padding: 8rpx 0; }
.advice-dot { color: #2196F3; margin-right: 8rpx; font-weight: bold; }
.advice-text { font-size: 28rpx; color: #555; line-height: 1.6; flex: 1; }
</style>
