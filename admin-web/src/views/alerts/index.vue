<template>
  <div class="alerts-page">
    <el-card class="filter-card">
      <el-form :inline="true" :model="filters">
        <el-form-item label="风险等级">
          <el-select v-model="filters.riskLevel" placeholder="全部" clearable style="width: 120px">
            <el-option label="高危" :value="3" />
            <el-option label="中危" :value="2" />
            <el-option label="低危" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="处置状态">
          <el-select v-model="filters.handleStatus" placeholder="全部" clearable style="width: 120px">
            <el-option label="未处理" :value="0" />
            <el-option label="已处理" :value="1" />
            <el-option label="误报取消" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 未处理高危告警置顶 -->
    <el-alert
      v-if="unhandledHighAlerts.length > 0"
      :title="`${unhandledHighAlerts.length}条高危告警待处理`"
      type="error"
      show-icon
      :closable="false"
      style="margin-bottom: 16px;"
    />

    <el-card>
      <el-table :data="alertList" stripe style="width: 100%">
        <el-table-column prop="alertTime" label="告警时间" width="170" sortable />
        <el-table-column label="老人" width="100">
          <template #default="{ row }">{{ row.userName || '--' }}</template>
        </el-table-column>
        <el-table-column label="告警类型" width="120">
          <template #default="{ row }">{{ alertTypeText(row.alertType) }}</template>
        </el-table-column>
        <el-table-column label="风险等级" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="riskTagType(row.riskLevel)" size="small">
              {{ riskText(row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column label="处置状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.handleStatus)" size="small">
              {{ statusText(row.handleStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <template v-if="row.handleStatus === 0">
              <el-button type="success" size="small" @click="markVisited(row)">标记已上门</el-button>
              <el-button type="primary" size="small" @click="phoneCallback(row)">电话回访</el-button>
              <el-button type="warning" size="small" @click="addRemark(row)">备注</el-button>
            </template>
            <span v-else class="handled-text">{{ row.handleResult || '已处置' }}</span>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        layout="total, prev, pager, next"
        style="margin-top: 16px; justify-content: flex-end;"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const filters = ref({ riskLevel: '', handleStatus: '' })
const pagination = ref({ page: 1, size: 20, total: 0 })

const alertList = ref([
  { id: 1, alertTime: '2026-06-22 12:15', userName: '张大爷', alertType: 1, riskLevel: 3, description: '疑似跌倒', handleStatus: 0, handleResult: '' },
  { id: 2, alertTime: '2026-06-22 11:40', userName: '李奶奶', alertType: 5, riskLevel: 2, description: '长时间静坐超3小时', handleStatus: 0, handleResult: '' },
  { id: 3, alertTime: '2026-06-22 10:20', userName: '王爷爷', alertType: 3, riskLevel: 2, description: '情绪低落', handleStatus: 0, handleResult: '' },
  { id: 4, alertTime: '2026-06-21 08:30', userName: '赵奶奶', alertType: 6, riskLevel: 1, description: '作息异常', handleStatus: 1, handleResult: '电话确认安全' },
  { id: 5, alertTime: '2026-06-20 03:15', userName: '周爷爷', alertType: 4, riskLevel: 1, description: '夜间异响', handleStatus: 2, handleResult: '误报，宠物活动' },
  { id: 6, alertTime: '2026-06-19 15:00', userName: '孙奶奶', alertType: 2, riskLevel: 3, description: '长时间不动超2小时', handleStatus: 1, handleResult: '已上门确认' }
])

pagination.value.total = alertList.value.length

const unhandledHighAlerts = computed(() =>
  alertList.value.filter(a => a.handleStatus === 0 && a.riskLevel === 3)
)

function alertTypeText(type) {
  const map = { 1: '疑似跌倒', 2: '长时间不动', 3: '情绪低落', 4: '夜间异响', 5: '长时间静坐', 6: '作息异常' }
  return map[type] || '异常'
}

function riskTagType(level) {
  return { 1: 'warning', 2: 'warning', 3: 'danger' }[level] || 'info'
}

function riskText(level) {
  return { 1: '低危', 2: '中危', 3: '高危' }[level] || '未知'
}

function statusTagType(status) {
  return { 0: 'danger', 1: 'success', 2: 'info' }[status] || 'info'
}

function statusText(status) {
  return { 0: '未处理', 1: '已处理', 2: '误报取消' }[status] || '未知'
}

function search() { ElMessage.info('查询已触发') }

function markVisited(row) {
  ElMessageBox.confirm(`确认已上门处置「${row.description}」？`, '标记已上门').then(() => {
    row.handleStatus = 1
    row.handleResult = '已上门确认安全'
    ElMessage.success('已标记')
  }).catch(() => {})
}

function phoneCallback(row) {
  ElMessageBox.prompt('请输入电话回访结果', '电话回访').then(({ value }) => {
    row.handleStatus = 1
    row.handleResult = `电话回访：${value}`
    ElMessage.success('回访已记录')
  }).catch(() => {})
}

function addRemark(row) {
  ElMessageBox.prompt('请输入处置备注', '备注处置结果').then(({ value }) => {
    row.handleResult = value
    row.handleStatus = 1
    ElMessage.success('备注已添加')
  }).catch(() => {})
}
</script>

<style scoped>
.filter-card { margin-bottom: 16px; }
.handled-text { font-size: 13px; color: #94a3b8; }
</style>
