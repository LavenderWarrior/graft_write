<template>
  <div class="dashboard">
    <!-- 顶部总数据看板 -->
    <el-row :gutter="20" class="stat-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon elder-icon">👴</div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.elderCount || 0 }}</div>
            <div class="stat-label">社区老年人口</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon alone-icon">🏠</div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.aloneCount || 0 }}</div>
            <div class="stat-label">独居老人</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon high-age-icon">🎂</div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.highAgeCount || 0 }}</div>
            <div class="stat-label">高龄老人(80+)</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card alert-stat">
          <div class="stat-icon alert-icon">🚨</div>
          <div class="stat-info">
            <div class="stat-value danger">{{ stats.todayAlertCount || 0 }}</div>
            <div class="stat-label">今日预警数量</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <!-- 左侧：辖区楼栋老人分布 -->
      <el-col :span="6">
        <el-card class="section-card">
          <template #header>
            <span class="card-title">辖区老人分布</span>
          </template>
          <div class="building-list">
            <div v-for="(b, idx) in buildings" :key="idx" class="building-item">
              <span class="building-name">{{ b.name }}</span>
              <el-progress :percentage="b.percent" :color="b.color" :stroke-width="8" />
              <span class="building-count">{{ b.count }}人</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 中间：可视化大屏图表 -->
      <el-col :span="12">
        <el-card class="section-card">
          <template #header>
            <span class="card-title">月度异常趋势</span>
          </template>
          <div ref="trendChart" class="chart-container"></div>
        </el-card>
        <el-card class="section-card" style="margin-top: 20px;">
          <template #header>
            <span class="card-title">睡眠/情绪统计</span>
          </template>
          <div ref="barChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 右侧：实时告警弹窗轮播 -->
      <el-col :span="6">
        <el-card class="section-card alert-panel">
          <template #header>
            <span class="card-title">
              <el-icon class="flash"><Bell /></el-icon>
              实时告警
            </span>
          </template>
          <div class="alert-scroll">
            <div v-for="(alert, idx) in realtimeAlerts" :key="idx"
                 class="realtime-alert" :class="'level-' + alert.riskLevel">
              <div class="ra-header">
                <el-tag :type="riskTagType(alert.riskLevel)" size="small">
                  {{ riskText(alert.riskLevel) }}
                </el-tag>
                <span class="ra-time">{{ alert.alertTime }}</span>
              </div>
              <div class="ra-desc">{{ alert.description || alertTypeText(alert.alertType) }}</div>
              <div class="ra-user">{{ alert.userName || '老人' }}</div>
              <el-button
                v-if="alert.handleStatus === 0"
                type="primary"
                size="small"
                @click="handleAlert(alert)"
              >
                处置
              </el-button>
            </div>
            <div v-if="realtimeAlerts.length === 0" class="no-alert">
              暂无告警
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 底部功能栏 -->
    <el-row class="bottom-bar">
      <el-button type="primary" @click="exportData">
        <el-icon><Download /></el-icon> 数据导出
      </el-button>
      <el-button @click="generateReport">
        <el-icon><Document /></el-icon> 批量生成政策申报材料
      </el-button>
      <el-button @click="systemSettings">
        <el-icon><Setting /></el-icon> 系统设置
      </el-button>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { Bell, Download, Document, Setting } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { ElMessage } from 'element-plus'

const trendChart = ref(null)
const barChart = ref(null)

const stats = ref({
  elderCount: 186,
  aloneCount: 42,
  highAgeCount: 28,
  todayAlertCount: 5
})

const buildings = ref([
  { name: '1号楼', count: 32, percent: 70, color: '#4CAF50' },
  { name: '2号楼', count: 28, percent: 60, color: '#2196F3' },
  { name: '3号楼', count: 45, percent: 95, color: '#FF9800' },
  { name: '4号楼', count: 22, percent: 48, color: '#9C27B0' },
  { name: '5号楼', count: 18, percent: 38, color: '#00BCD4' },
  { name: '6号楼', count: 35, percent: 75, color: '#F44336' },
  { name: '7号楼', count: 6, percent: 12, color: '#607D8B' }
])

const realtimeAlerts = ref([
  { id: 1, riskLevel: 3, alertType: 1, alertTime: '12:15', description: '3号楼张大爷疑似跌倒', handleStatus: 0, userName: '张大爷' },
  { id: 2, riskLevel: 2, alertType: 5, alertTime: '11:40', description: '1号楼李奶奶长时间静坐', handleStatus: 0, userName: '李奶奶' },
  { id: 3, riskLevel: 2, alertType: 3, alertTime: '10:20', description: '5号楼王爷爷情绪低落', handleStatus: 0, userName: '王爷爷' },
  { id: 4, riskLevel: 1, alertType: 6, alertTime: '08:30', description: '2号楼赵奶奶作息异常', handleStatus: 1, userName: '赵奶奶' },
  { id: 5, riskLevel: 1, alertType: 4, alertTime: '03:15', description: '6号楼周爷爷夜间异响', handleStatus: 1, userName: '周爷爷' }
])

function riskTagType(level) {
  const map = { 1: 'warning', 2: 'warning', 3: 'danger' }
  return map[level] || 'info'
}

function riskText(level) {
  const map = { 1: '低危', 2: '中危', 3: '高危' }
  return map[level] || '未知'
}

function alertTypeText(type) {
  const map = { 1: '疑似跌倒', 2: '长时间不动', 3: '情绪低落', 4: '夜间异响', 5: '长时间静坐', 6: '作息异常' }
  return map[type] || '异常'
}

function handleAlert(alert) {
  ElMessage.success(`已处置：${alert.description}`)
  alert.handleStatus = 1
}

function exportData() {
  ElMessage.info('数据导出功能开发中')
}
function generateReport() {
  ElMessage.info('政策申报材料生成中...')
}
function systemSettings() {
  ElMessage.info('系统设置功能开发中')
}

function initTrendChart() {
  if (!trendChart.value) return
  const chart = echarts.init(trendChart.value)
  chart.setOption({
    tooltip: { trigger: 'axis' },
    legend: { data: ['跌倒', '情绪异常', '作息异常', '夜间异响'] },
    xAxis: {
      type: 'category',
      data: ['第1周', '第2周', '第3周', '第4周']
    },
    yAxis: { type: 'value', name: '次数' },
    series: [
      { name: '跌倒', type: 'line', data: [2, 1, 3, 1], smooth: true, itemStyle: { color: '#F44336' } },
      { name: '情绪异常', type: 'line', data: [5, 8, 6, 4], smooth: true, itemStyle: { color: '#FF9800' } },
      { name: '作息异常', type: 'line', data: [8, 6, 9, 7], smooth: true, itemStyle: { color: '#FFC107' } },
      { name: '夜间异响', type: 'line', data: [3, 2, 4, 2], smooth: true, itemStyle: { color: '#9C27B0' } }
    ]
  })
  window.addEventListener('resize', () => chart.resize())
}

function initBarChart() {
  if (!barChart.value) return
  const chart = echarts.init(barChart.value)
  chart.setOption({
    tooltip: { trigger: 'axis' },
    legend: { data: ['平均睡眠评分', '情绪正常占比(%)'] },
    xAxis: {
      type: 'category',
      data: ['1号楼', '2号楼', '3号楼', '4号楼', '5号楼', '6号楼', '7号楼']
    },
    yAxis: { type: 'value' },
    series: [
      { name: '平均睡眠评分', type: 'bar', data: [72, 68, 75, 80, 65, 70, 78], itemStyle: { color: '#7C4DFF' } },
      { name: '情绪正常占比(%)', type: 'bar', data: [85, 78, 90, 82, 75, 88, 92], itemStyle: { color: '#4CAF50' } }
    ]
  })
  window.addEventListener('resize', () => chart.resize())
}

onMounted(() => {
  nextTick(() => {
    initTrendChart()
    initBarChart()
  })
})
</script>

<style scoped>
.dashboard { }

.stat-row { margin-bottom: 20px; }
.stat-card {
  display: flex; align-items: center; padding: 8px;
}
.stat-card :deep(.el-card__body) {
  display: flex; align-items: center; width: 100%;
}
.stat-icon { font-size: 40px; margin-right: 16px; }
.stat-info { flex: 1; }
.stat-value { font-size: 32px; font-weight: bold; color: #1e293b; line-height: 1.2; }
.stat-value.danger { color: #F44336; }
.stat-label { font-size: 13px; color: #94a3b8; margin-top: 4px; }

.section-card { height: auto; }
.card-title { font-size: 15px; font-weight: 600; color: #1e293b; }

.building-list { }
.building-item {
  display: flex; align-items: center; gap: 8px;
  margin-bottom: 12px; font-size: 13px;
}
.building-name { width: 50px; color: #64748b; white-space: nowrap; }
.building-count { width: 40px; text-align: right; color: #94a3b8; font-size: 12px; }
.building-item :deep(.el-progress) { flex: 1; }

.chart-container { height: 280px; width: 100%; }

.alert-panel { }
.flash { animation: flash 2s infinite; color: #F44336; }
@keyframes flash {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.3; }
}

.alert-scroll { max-height: 600px; overflow-y: auto; }
.realtime-alert {
  padding: 12px; margin-bottom: 12px; border-radius: 8px;
  background: #f8fafc; border-left: 4px solid #ccc;
}
.realtime-alert.level-1 { border-left-color: #FFC107; }
.realtime-alert.level-2 { border-left-color: #FF9800; }
.realtime-alert.level-3 { border-left-color: #F44336; background: #FFF5F5; }

.ra-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px; }
.ra-time { font-size: 12px; color: #94a3b8; }
.ra-desc { font-size: 13px; color: #334155; margin-bottom: 4px; }
.ra-user { font-size: 12px; color: #94a3b8; margin-bottom: 8px; }
.no-alert { text-align: center; color: #ccc; padding: 40px 0; }

.bottom-bar {
  margin-top: 20px; padding: 16px 0;
  display: flex; gap: 12px;
  border-top: 1px solid #e5e7eb;
}
</style>
