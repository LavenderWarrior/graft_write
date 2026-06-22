<template>
  <view class="profile-page">
    <!-- 用户信息 -->
    <view class="card user-card">
      <view class="user-avatar">👨</view>
      <view class="user-info">
        <text class="user-name">{{ userName }}</text>
        <text class="user-role">子女监护人</text>
      </view>
    </view>

    <!-- 消息推送设置 -->
    <view class="card section-card">
      <view class="section-title">消息推送设置</view>
      <view class="setting-item">
        <text class="setting-label">💬 微信服务通知</text>
        <switch :checked="wechatNotify" @change="toggleWechat" color="#2196F3" />
      </view>
      <view class="setting-item">
        <text class="setting-label">📱 短信紧急通知</text>
        <switch :checked="smsNotify" @change="toggleSms" color="#2196F3" />
      </view>
    </view>

    <!-- 多老人管理 -->
    <view class="card section-card">
      <view class="section-title">绑定老人管理</view>
      <view v-if="elders.length === 0" class="empty-text">暂未绑定老人</view>
      <view v-for="(elder, idx) in elders" :key="idx" class="elder-item">
        <text class="elder-avatar">👴</text>
        <view class="elder-info">
          <text class="elder-name">{{ elder.realName || '老人' + (idx + 1) }}</text>
          <text class="elder-status">{{ elder.monitorOn ? '监护中' : '未监护' }}</text>
        </view>
        <view class="elder-action" @tap="switchToElder(idx)">查看</view>
      </view>
      <view class="add-elder-btn" @tap="addElder">
        <text>+ 绑定新的老人</text>
      </view>
    </view>

    <!-- 会员续费 -->
    <view class="card section-card member-card" @tap="goMembership">
      <view class="member-row">
        <text class="member-icon">👑</text>
        <text class="member-text">会员续费入口</text>
      </view>
      <text class="member-arrow">›</text>
    </view>

    <!-- 其他操作 -->
    <view class="card section-card">
      <view class="setting-item clickable" @tap="about">
        <text class="setting-label">ℹ️ 关于我们</text>
        <text class="setting-arrow">›</text>
      </view>
      <view class="setting-item clickable" @tap="logout">
        <text class="setting-label">🚪 退出登录</text>
        <text class="setting-arrow">›</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userName: '张先生',
      wechatNotify: true,
      smsNotify: false,
      elders: [
        { realName: '张奶奶', monitorOn: true },
        { realName: '张爷爷', monitorOn: false }
      ]
    }
  },
  methods: {
    toggleWechat(e) { this.wechatNotify = e.detail.value },
    toggleSms(e) { this.smsNotify = e.detail.value },
    switchToElder(idx) {
      uni.switchTab({ url: '/pages/home/index' })
    },
    addElder() {
      uni.scanCode({
        success: () => {
          uni.showToast({ title: '绑定成功', icon: 'success' })
        },
        fail: () => {
          uni.showToast({ title: '请使用微信扫码绑定', icon: 'none' })
        }
      })
    },
    goMembership() {
      uni.showToast({ title: '会员续费功能开发中', icon: 'none' })
    },
    about() {
      uni.showModal({
        title: '关于',
        content: '银发健康监护 - 子女监护端 v1.0.0\n让远方的牵挂更放心',
        showCancel: false
      })
    },
    logout() {
      uni.showModal({
        title: '退出登录',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.clearStorageSync()
            uni.reLaunch({ url: '/pages/login/index' })
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.profile-page { min-height: 100vh; background: #f5f5f5; padding-bottom: 140rpx; }

.user-card { display: flex; align-items: center; margin-top: 16rpx; }
.user-avatar { font-size: 72rpx; margin-right: 20rpx; }
.user-name { display: block; font-size: 36rpx; font-weight: bold; color: #333; }
.user-role { display: block; font-size: 26rpx; color: #2196F3; margin-top: 4rpx; }

.section-card { margin-top: 16rpx; }
.section-title { font-size: 32rpx; font-weight: bold; color: #333; margin-bottom: 16rpx; }

.setting-item {
  display: flex; align-items: center; justify-content: space-between;
  padding: 20rpx 0; border-bottom: 1rpx solid #f5f5f5;
}
.setting-item:last-child { border-bottom: none; }
.setting-label { font-size: 30rpx; color: #333; }
.setting-arrow { font-size: 36rpx; color: #ccc; }

.empty-text { text-align: center; color: #ccc; font-size: 28rpx; padding: 24rpx 0; }

.elder-item {
  display: flex; align-items: center; padding: 20rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}
.elder-avatar { font-size: 48rpx; margin-right: 16rpx; }
.elder-info { flex: 1; }
.elder-name { display: block; font-size: 30rpx; color: #333; }
.elder-status { display: block; font-size: 24rpx; color: #999; margin-top: 2rpx; }
.elder-action {
  font-size: 26rpx; color: #2196F3; padding: 8rpx 16rpx;
  border: 1rpx solid #2196F3; border-radius: 10rpx;
}
.add-elder-btn {
  text-align: center; padding: 20rpx 0; color: #2196F3;
  font-size: 30rpx; margin-top: 8rpx;
}

.member-card {
  display: flex; align-items: center; justify-content: space-between;
  background: linear-gradient(135deg, #FFF8E1, #FFECB3);
  border: 2rpx solid #FFD54F;
}
.member-row { display: flex; align-items: center; }
.member-icon { font-size: 40rpx; margin-right: 12rpx; }
.member-text { font-size: 30rpx; font-weight: bold; color: #F57F17; }
.member-arrow { font-size: 36rpx; color: #F9A825; }
</style>
