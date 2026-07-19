<template>
  <div class="elder-detail">
    <el-page-header @back="goBack" title="返回列表" :content="elder.realName + ' - 详情管理'" />

    <el-row :gutter="20" style="margin-top: 20px;">
      <!-- 基础信息 -->
      <el-col :span="8">
        <el-card>
          <template #header>
            <span class="card-title">基础信息</span>
          </template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="姓名">{{ elder.realName }}</el-descriptions-item>
            <el-descriptions-item label="年龄">{{ elder.age }}岁</el-descriptions-item>
            <el-descriptions-item label="性别">{{ elder.gender === 1 ? '男' : '女' }}</el-descriptions-item>
            <el-descriptions-item label="电话">{{ elder.phone }}</el-descriptions-item>
            <el-descriptions-item label="住址">{{ elder.address }}</el-descriptions-item>
            <el-descriptions-item label="独居">
              <el-tag v-if="elder.isAlone" type="danger" size="small">是</el-tag>
              <span v-else>否</span>
            </el-descriptions-item>
            <el-descriptions-item label="标记">
              <el-tag v-if="elder.isDisabled" type="warning" size="small">失能</el-tag>
              <el-tag v-if="elder.isLowIncome" type="info" size="small">低保</el-tag>
              <span v-if="!elder.isDisabled && !elder.isLowIncome">无</span>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>

      <!-- 风险告警记录 -->
      <el-col :span="16">
        <el-card>
          <template #header>
            <div class="card-header-row">
              <span class="card-title">历史风险告警记录</span>
              <el-button type="primary" size="small" @click="exportReport">
                导出月度健康报表
              </el-button>
            </div>
          </template>
          <el-table :data="alerts" stripe max-height="300">
            <el-table-column prop="alertTime" label="时间" width="160" />
            <el-table-column label="类型" width="120">
              <template #default="{ row }">
                {{ alertTypeText(row.alertType) }}
              </template>
            </el-table-column>
            <el-table-column label="风险等级" width="100">
              <template #default="{ row }">
                <el-tag :type="riskTagType(row.riskLevel)" size="small">
                  {{ riskText(row.riskLevel) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="description" label="描述" show-overflow-tooltip />
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.handleStatus === 1 ? 'success' : 'danger'" size="small">
                  {{ row.handleStatus === 1 ? '已处理' : '未处理' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 上门帮扶登记 -->
    <el-card style="margin-top: 20px;">
      <template #header>
        <div class="card-header-row">
          <span class="card-title">上门帮扶记录</span>
          <el-button type="success" size="small" @click="showAddVisit = true">新增帮扶记录</el-button>
        </div>
      </template>
      <el-table :data="visits" stripe>
        <el-table-column prop="visitTime" label="时间" width="160" />
        <el-table-column label="类型" width="120">
          <template #default="{ row }">
            {{ visitTypeText(row.visitType) }}
          </template>
        </el-table-column>
        <el-table-column prop="content" label="帮扶内容" show-overflow-tooltip />
        <el-table-column prop="result" label="回访结果" show-overflow-tooltip />
      </el-table>
    </el-card>

    <!-- 新增帮扶弹窗 -->
    <el-dialog v-model="showAddVisit" title="新增帮扶记录" width="500px">
      <el-form :model="visitForm" label-width="100px">
        <el-form-item label="帮扶类型">
          <el-select v-model="visitForm.visitType" style="width: 100%">
            <el-option label="日常巡访" :value="1" />
            <el-option label="告警响应" :value="2" />
            <el-option label="帮扶服务" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="帮扶内容">
          <el-input v-model="visitForm.content" type="textarea" rows="3" placeholder="请填写帮扶内容" />
        </el-form-item>
        <el-form-item label="回访结果">
          <el-input v-model="visitForm.result" type="textarea" rows="2" placeholder="请填写回访结果" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddVisit = false">取消</el-button>
        <el-button type="primary" @click="submitVisit">确认提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const elder = ref({
  id: route.params.id,
  realName: '张大爷',
  age: 78,
  gender: 1,
  phone: '138****1234',
  address: '3号楼2单元501',
  isAlone: 1,
  isDisabled: 0,
  isLowIncome: 0
})

const alerts = ref([
  { alertTime: '2026-06-22 12:15', alertType: 1, riskLevel: 3, description: '疑似跌倒', handleStatus: 0 },
  { alertTime: '2026-06-20 14:30', alertType: 5, riskLevel: 2, description: '长时间静坐超3小时', handleStatus: 1 },
  { alertTime: '2026-06-18 19:45', alertType: 3, riskLevel: 2, description: '情绪低落', handleStatus: 1 },
  { alertTime: '2026-06-15 07:00', alertType: 6, riskLevel: 1, description: '起床时间偏晚', handleStatus: 1 }
])

const visits = ref([
  { visitTime: '2026-06-20 15:00', visitType: 2, content: '告警响应，确认老人安全', result: '老人状态正常' },
  { visitTime: '2026-06-15 10:00', visitType: 1, content: '日常巡访，检查居住环境', result: '环境良好，建议增加活动量' }
])

const showAddVisit = ref(false)
const visitForm = ref({ visitType: 1, content: '', result: '' })

function goBack() { router.push('/elders') }

function alertTypeText(type) {
  const map = { 1: '疑似跌倒', 2: '长时间不动', 3: '情绪低落', 4: '夜间异响', 5: '长时间静坐', 6: '作息异常' }
  return map[type] || '异常'
}

function riskTagType(level) {
  const map = { 1: 'warning', 2: 'warning', 3: 'danger' }
  return map[level] || 'info'
}

function riskText(level) {
  const map = { 1: '低危', 2: '中危', 3: '高危' }
  return map[level] || '未知'
}

function visitTypeText(type) {
  const map = { 1: '日常巡访', 2: '告警响应', 3: '帮扶服务' }
  return map[type] || '其他'
}

function exportReport() {
  ElMessage.success('月度健康报表生成中...')
}

function submitVisit() {
  if (!visitForm.value.content) {
    ElMessage.warning('请填写帮扶内容')
    return
  }
  visits.value.unshift({
    visitTime: new Date().toISOString().slice(0, 16).replace('T', ' '),
    visitType: visitForm.value.visitType,
    content: visitForm.value.content,
    result: visitForm.value.result
  })
  showAddVisit.value = false
  visitForm.value = { visitType: 1, content: '', result: '' }
  ElMessage.success('帮扶记录已添加')
}
</script>

<style scoped>
.card-title { font-size: 15px; font-weight: 600; }
.card-header-row { display: flex; justify-content: space-between; align-items: center; }
</style>
