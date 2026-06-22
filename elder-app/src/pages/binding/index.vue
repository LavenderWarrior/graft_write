<template>
  <view class="binding-page">
    <!-- 绑定方式 -->
    <view class="card">
      <view class="section-title">绑定方式</view>

      <view class="bind-method" @tap="showQRCode">
        <view class="method-icon">📱</view>
        <view class="method-info">
          <text class="method-title">子女扫码绑定</text>
          <text class="method-desc">展示二维码，让子女用微信扫码绑定</text>
        </view>
        <text class="method-arrow">›</text>
      </view>

      <view class="bind-method" @tap="showManualBind">
        <view class="method-icon">✏️</view>
        <view class="method-info">
          <text class="method-title">手动输入微信ID绑定</text>
          <text class="method-desc">输入子女的微信ID进行绑定</text>
        </view>
        <text class="method-arrow">›</text>
      </view>

      <view class="bind-method" @tap="communityBind">
        <view class="method-icon">🏘️</view>
        <view class="method-info">
          <text class="method-title">社区网格员邀请绑定</text>
          <text class="method-desc">由社区网格员发起绑定邀请</text>
        </view>
        <text class="method-arrow">›</text>
      </view>
    </view>

    <!-- 已绑定列表 -->
    <view class="card">
      <view class="section-title">已绑定监护人</view>
      <view v-if="bindings.length === 0" class="empty-text">
        暂未绑定监护人
      </view>
      <view v-for="(item, idx) in bindings" :key="idx" class="bind-item">
        <view class="bind-avatar">{{ item.guardianType === 1 ? '👨' : '🏘️' }}</view>
        <view class="bind-info">
          <text class="bind-name">{{ item.guardianName || '监护人' + (idx + 1) }}</text>
          <text class="bind-perm">{{ permissionText(item.permission) }}</text>
        </view>
        <view class="unbind-btn" @tap="unbind(item)">解除绑定</view>
      </view>
    </view>

    <!-- 二维码弹窗 -->
    <view class="dialog-mask" v-if="showQR" @tap="showQR = false">
      <view class="qr-dialog" @tap.stop>
        <view class="qr-title">扫码绑定</view>
        <view class="qr-code-area">
          <view class="qr-placeholder">
            <text class="qr-code-text">{{ bindCode }}</text>
            <text class="qr-hint">请子女打开监护端扫描此码</text>
          </view>
        </view>
        <view class="qr-close" @tap="showQR = false">关闭</view>
      </view>
    </view>

    <!-- 手动绑定弹窗 -->
    <view class="dialog-mask" v-if="showManual" @tap="showManual = false">
      <view class="manual-dialog" @tap.stop>
        <view class="manual-title">输入微信ID绑定</view>
        <input class="manual-input" v-model="wechatId" placeholder="请输入子女微信ID" />
        <view class="manual-buttons">
          <view class="manual-btn cancel" @tap="showManual = false">取消</view>
          <view class="manual-btn confirm" @tap="confirmManualBind">确认绑定</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import api from '../../common/api.js'

export default {
  data() {
    return {
      bindings: [],
      showQR: false,
      showManual: false,
      bindCode: '',
      wechatId: ''
    }
  },
  methods: {
    permissionText(perm) {
      const map = { 1: '仅查看', 2: '接收告警', 3: '全部权限' }
      return map[perm] || '仅查看'
    },
    showQRCode() {
      const userId = uni.getStorageSync('userId')
      if (!userId) {
        this.bindCode = 'DEMO1234'
        this.showQR = true
        return
      }
      api.generateBindCode(userId).then(code => {
        this.bindCode = code
        this.showQR = true
      }).catch(() => {
        this.bindCode = 'ERROR'
        this.showQR = true
      })
    },
    showManualBind() {
      this.wechatId = ''
      this.showManual = true
    },
    confirmManualBind() {
      if (!this.wechatId.trim()) {
        uni.showToast({ title: '请输入微信ID', icon: 'none' })
        return
      }
      this.showManual = false
      uni.showToast({ title: '绑定请求已发送', icon: 'success' })
    },
    communityBind() {
      uni.showToast({ title: '请联系社区网格员发起绑定', icon: 'none' })
    },
    unbind(item) {
      uni.showModal({
        title: '确认解除绑定',
        content: `确定要解除与${item.guardianName || '该监护人'}的绑定关系吗？`,
        confirmColor: '#F44336',
        success: (res) => {
          if (res.confirm) {
            api.unbind(item.id).then(() => {
              uni.showToast({ title: '已解除绑定', icon: 'success' })
              this.loadBindings()
            }).catch(() => {})
          }
        }
      })
    },
    loadBindings() {
      const userId = uni.getStorageSync('userId')
      if (!userId) return
      api.getElderBindings(userId).then(data => {
        this.bindings = data || []
      }).catch(() => {})
    }
  },
  onShow() {
    this.loadBindings()
  }
}
</script>

<style scoped>
.binding-page { min-height: 100vh; background: #f5f5f5; }
.section-title { font-size: 34rpx; font-weight: bold; color: #333; margin-bottom: 20rpx; }

.bind-method {
  display: flex; align-items: center; padding: 28rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}
.bind-method:last-child { border-bottom: none; }
.method-icon { font-size: 52rpx; margin-right: 20rpx; }
.method-info { flex: 1; }
.method-title { display: block; font-size: 34rpx; font-weight: bold; color: #333; }
.method-desc { display: block; font-size: 28rpx; color: #999; margin-top: 4rpx; }
.method-arrow { font-size: 40rpx; color: #ccc; }

.empty-text { text-align: center; color: #ccc; font-size: 30rpx; padding: 40rpx 0; }

.bind-item {
  display: flex; align-items: center; padding: 24rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}
.bind-item:last-child { border-bottom: none; }
.bind-avatar { font-size: 52rpx; margin-right: 20rpx; }
.bind-info { flex: 1; }
.bind-name { display: block; font-size: 34rpx; color: #333; }
.bind-perm { display: block; font-size: 26rpx; color: #999; margin-top: 4rpx; }
.unbind-btn {
  font-size: 28rpx; color: #F44336; padding: 8rpx 20rpx;
  border: 2rpx solid #F44336; border-radius: 12rpx;
}

.dialog-mask {
  position: fixed; top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.6); display: flex;
  align-items: center; justify-content: center; z-index: 999;
}
.qr-dialog {
  width: 560rpx; background: #fff; border-radius: 28rpx;
  padding: 48rpx; text-align: center;
}
.qr-title { font-size: 38rpx; font-weight: bold; color: #333; margin-bottom: 32rpx; }
.qr-code-area {
  width: 400rpx; height: 400rpx; margin: 0 auto 24rpx;
  background: #f5f5f5; border-radius: 16rpx;
  display: flex; align-items: center; justify-content: center;
}
.qr-code-text { display: block; font-size: 56rpx; font-weight: bold; color: #4CAF50; letter-spacing: 8rpx; }
.qr-hint { display: block; font-size: 28rpx; color: #999; margin-top: 16rpx; }
.qr-close {
  font-size: 32rpx; color: #999; padding: 16rpx;
}

.manual-dialog {
  width: 560rpx; background: #fff; border-radius: 28rpx; padding: 48rpx;
}
.manual-title { font-size: 38rpx; font-weight: bold; color: #333; margin-bottom: 32rpx; text-align: center; }
.manual-input {
  border: 2rpx solid #ddd; border-radius: 16rpx; padding: 24rpx;
  font-size: 34rpx; margin-bottom: 32rpx;
}
.manual-buttons { display: flex; gap: 20rpx; }
.manual-btn {
  flex: 1; text-align: center; padding: 24rpx 0;
  border-radius: 20rpx; font-size: 34rpx; font-weight: bold;
}
.manual-btn.cancel { background: #f5f5f5; color: #999; }
.manual-btn.confirm { background: #4CAF50; color: #fff; }
</style>
