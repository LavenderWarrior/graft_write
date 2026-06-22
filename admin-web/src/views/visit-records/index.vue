<template>
  <div class="visits-page">
    <el-card class="filter-card">
      <el-form :inline="true">
        <el-form-item label="老人姓名">
          <el-input v-model="keyword" placeholder="搜索老人姓名" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="帮扶类型">
          <el-select v-model="visitType" placeholder="全部" clearable style="width: 140px">
            <el-option label="日常巡访" :value="1" />
            <el-option label="告警响应" :value="2" />
            <el-option label="帮扶服务" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
          <el-button type="success" @click="showAddDialog = true">新增帮扶记录</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table :data="visitList" stripe>
        <el-table-column prop="visitTime" label="上门时间" width="170" sortable />
        <el-table-column prop="elderName" label="老人姓名" width="100" />
        <el-table-column prop="workerName" label="工作人员" width="100" />
        <el-table-column label="帮扶类型" width="120">
          <template #default="{ row }">
            <el-tag :type="visitTagType(row.visitType)" size="small">
              {{ visitTypeText(row.visitType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="帮扶内容" show-overflow-tooltip />
        <el-table-column prop="result" label="回访结果" show-overflow-tooltip />
      </el-table>

      <el-pagination
        v-model:current-page="page"
        :total="total"
        layout="total, prev, pager, next"
        style="margin-top: 16px; justify-content: flex-end;"
      />
    </el-card>

    <!-- 新增帮扶弹窗 -->
    <el-dialog v-model="showAddDialog" title="新增帮扶记录" width="520px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="老人">
          <el-select v-model="form.elderId" placeholder="选择老人" style="width: 100%">
            <el-option label="张大爷" :value="1" />
            <el-option label="李奶奶" :value="2" />
            <el-option label="王爷爷" :value="3" />
            <el-option label="赵奶奶" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="帮扶类型">
          <el-select v-model="form.visitType" style="width: 100%">
            <el-option label="日常巡访" :value="1" />
            <el-option label="告警响应" :value="2" />
            <el-option label="帮扶服务" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="帮扶内容">
          <el-input v-model="form.content" type="textarea" rows="3" />
        </el-form-item>
        <el-form-item label="回访结果">
          <el-input v-model="form.result" type="textarea" rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="submitVisit">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const keyword = ref('')
const visitType = ref('')
const page = ref(1)
const total = ref(0)
const showAddDialog = ref(false)
const form = ref({ elderId: '', visitType: 1, content: '', result: '' })

const visitList = ref([
  { visitTime: '2026-06-22 10:30', elderName: '张大爷', workerName: '李网格员', visitType: 2, content: '跌倒告警响应，上门确认', result: '老人安全，建议安装扶手' },
  { visitTime: '2026-06-20 14:00', elderName: '李奶奶', workerName: '李网格员', visitType: 3, content: '帮扶送餐服务', result: '已送达午餐' },
  { visitTime: '2026-06-18 09:00', elderName: '王爷爷', workerName: '张社工', visitType: 1, content: '日常巡访检查居住环境', result: '环境良好' },
  { visitTime: '2026-06-15 15:30', elderName: '赵奶奶', workerName: '李网格员', visitType: 1, content: '日常巡访', result: '老人状态良好，家属已来探望' },
  { visitTime: '2026-06-12 11:00', elderName: '孙奶奶', workerName: '张社工', visitType: 3, content: '协助办理低保续期', result: '材料已提交' }
])

total.value = visitList.value.length

function visitTypeText(type) {
  return { 1: '日常巡访', 2: '告警响应', 3: '帮扶服务' }[type] || '其他'
}

function visitTagType(type) {
  return { 1: '', 2: 'danger', 3: 'success' }[type] || 'info'
}

function search() { ElMessage.info('查询已触发') }

function submitVisit() {
  if (!form.value.elderId || !form.value.content) {
    ElMessage.warning('请填写完整信息')
    return
  }
  const elderNames = { 1: '张大爷', 2: '李奶奶', 3: '王爷爷', 4: '赵奶奶' }
  visitList.value.unshift({
    visitTime: new Date().toISOString().slice(0, 16).replace('T', ' '),
    elderName: elderNames[form.value.elderId],
    workerName: '当前管理员',
    visitType: form.value.visitType,
    content: form.value.content,
    result: form.value.result
  })
  total.value++
  showAddDialog.value = false
  form.value = { elderId: '', visitType: 1, content: '', result: '' }
  ElMessage.success('帮扶记录已添加')
}
</script>

<style scoped>
.filter-card { margin-bottom: 16px; }
</style>
