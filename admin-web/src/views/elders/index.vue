<template>
  <div class="elders-page">
    <!-- 筛选条件 -->
    <el-card class="filter-card">
      <el-form :inline="true" :model="filters">
        <el-form-item label="搜索">
          <el-input v-model="filters.keyword" placeholder="姓名/电话" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="年龄段">
          <el-select v-model="filters.ageRange" placeholder="全部" clearable style="width: 120px">
            <el-option label="60-70岁" value="60-70" />
            <el-option label="70-80岁" value="70-80" />
            <el-option label="80岁以上" value="80+" />
          </el-select>
        </el-form-item>
        <el-form-item label="独居">
          <el-select v-model="filters.isAlone" placeholder="全部" clearable style="width: 100px">
            <el-option label="是" :value="1" />
            <el-option label="否" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="近期异常">
          <el-select v-model="filters.hasAlert" placeholder="全部" clearable style="width: 100px">
            <el-option label="有" :value="1" />
            <el-option label="无" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
          <el-button @click="resetFilters">重置</el-button>
          <el-button type="success" @click="addElder">新增老人档案</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 老人列表 -->
    <el-card class="list-card">
      <el-table :data="elderList" stripe style="width: 100%">
        <el-table-column prop="realName" label="姓名" width="100" />
        <el-table-column prop="age" label="年龄" width="80" />
        <el-table-column prop="gender" label="性别" width="70">
          <template #default="{ row }">
            {{ row.gender === 1 ? '男' : row.gender === 2 ? '女' : '未知' }}
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="联系方式" width="130" />
        <el-table-column prop="address" label="住址" min-width="160" show-overflow-tooltip />
        <el-table-column label="独居" width="70" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.isAlone" type="danger" size="small">独居</el-tag>
            <span v-else>否</span>
          </template>
        </el-table-column>
        <el-table-column label="标记" width="120">
          <template #default="{ row }">
            <el-tag v-if="row.isDisabled" type="warning" size="small">失能</el-tag>
            <el-tag v-if="row.isLowIncome" type="info" size="small">低保</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="近7天健康" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="healthTagType(row.healthStatus)" size="small">
              {{ healthText(row.healthStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="viewDetail(row)">详情</el-button>
            <el-button type="success" link size="small" @click="addVisit(row)">上门帮扶</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next"
        :page-sizes="[10, 20, 50]"
        style="margin-top: 16px; justify-content: flex-end;"
        @current-change="loadList"
        @size-change="loadList"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

const filters = ref({
  keyword: '',
  ageRange: '',
  isAlone: '',
  hasAlert: ''
})

const pagination = ref({ page: 1, size: 10, total: 0 })

const elderList = ref([
  { id: 1, realName: '张大爷', age: 78, gender: 1, phone: '138****1234', address: '3号楼2单元501', isAlone: 1, isDisabled: 0, isLowIncome: 0, healthStatus: 'warning' },
  { id: 2, realName: '李奶奶', age: 82, gender: 2, phone: '139****5678', address: '1号楼1单元302', isAlone: 1, isDisabled: 1, isLowIncome: 1, healthStatus: 'danger' },
  { id: 3, realName: '王爷爷', age: 75, gender: 1, phone: '137****9012', address: '5号楼3单元101', isAlone: 0, isDisabled: 0, isLowIncome: 0, healthStatus: 'normal' },
  { id: 4, realName: '赵奶奶', age: 85, gender: 2, phone: '136****3456', address: '2号楼2单元603', isAlone: 1, isDisabled: 0, isLowIncome: 1, healthStatus: 'warning' },
  { id: 5, realName: '周爷爷', age: 70, gender: 1, phone: '135****7890', address: '6号楼1单元402', isAlone: 0, isDisabled: 0, isLowIncome: 0, healthStatus: 'normal' },
  { id: 6, realName: '孙奶奶', age: 88, gender: 2, phone: '133****2345', address: '4号楼3单元201', isAlone: 1, isDisabled: 1, isLowIncome: 1, healthStatus: 'danger' }
])

pagination.value.total = elderList.value.length

function healthTagType(status) {
  const map = { normal: 'success', warning: 'warning', danger: 'danger' }
  return map[status] || 'info'
}

function healthText(status) {
  const map = { normal: '正常', warning: '注意', danger: '异常' }
  return map[status] || '未知'
}

function search() {
  ElMessage.info('查询功能已触发')
}

function resetFilters() {
  filters.value = { keyword: '', ageRange: '', isAlone: '', hasAlert: '' }
}

function addElder() {
  ElMessage.info('新增老人档案功能开发中')
}

function viewDetail(row) {
  router.push(`/elder/${row.id}`)
}

function addVisit(row) {
  ElMessageBox.prompt('请输入帮扶内容', '登记上门帮扶', {
    confirmButtonText: '确认登记',
    cancelButtonText: '取消'
  }).then(({ value }) => {
    ElMessage.success(`已登记对${row.realName}的帮扶记录`)
  }).catch(() => {})
}

function loadList() {}

onMounted(() => {})
</script>

<style scoped>
.filter-card { margin-bottom: 16px; }
.list-card { }
</style>
