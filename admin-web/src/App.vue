<template>
  <el-container class="app-container">
    <!-- 侧边栏 -->
    <el-aside width="220px" class="app-aside">
      <div class="logo-area">
        <span class="logo-icon">🏥</span>
        <span class="logo-text">银发监护后台</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#1e293b"
        text-color="#94a3b8"
        active-text-color="#ffffff"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataBoard /></el-icon>
          <span>数据看板</span>
        </el-menu-item>
        <el-menu-item index="/elders">
          <el-icon><User /></el-icon>
          <span>老人档案</span>
        </el-menu-item>
        <el-menu-item index="/alerts">
          <el-icon><Bell /></el-icon>
          <span>告警台账</span>
        </el-menu-item>
        <el-menu-item index="/visits">
          <el-icon><Document /></el-icon>
          <span>帮扶记录</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主内容区 -->
    <el-container>
      <el-header class="app-header">
        <span class="header-title">{{ pageTitle }}</span>
        <div class="header-right">
          <el-badge :value="alertCount" :hidden="alertCount === 0" class="alert-badge">
            <el-icon :size="20"><Bell /></el-icon>
          </el-badge>
          <span class="admin-name">社区管理员</span>
        </div>
      </el-header>
      <el-main class="app-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { DataBoard, User, Bell, Document } from '@element-plus/icons-vue'

const route = useRoute()
const alertCount = ref(3)

const activeMenu = computed(() => route.path)

const titleMap = {
  '/dashboard': '数据看板',
  '/elders': '辖区老人档案',
  '/alerts': '告警处置台账',
  '/visits': '上门帮扶记录'
}
const pageTitle = computed(() => {
  for (const [path, title] of Object.entries(titleMap)) {
    if (route.path.startsWith(path)) return title
  }
  return '社区管理后台'
})
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
html, body, #app { height: 100%; }

.app-container { height: 100vh; }

.app-aside {
  background: #1e293b;
  overflow-y: auto;
}
.logo-area {
  padding: 20px 16px;
  display: flex;
  align-items: center;
  border-bottom: 1px solid #334155;
}
.logo-icon { font-size: 28px; margin-right: 8px; }
.logo-text { color: #fff; font-size: 16px; font-weight: bold; }

.el-menu { border-right: none !important; }

.app-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #e5e7eb;
  background: #fff;
  padding: 0 24px;
}
.header-title { font-size: 18px; font-weight: 600; color: #1e293b; }
.header-right { display: flex; align-items: center; gap: 16px; }
.alert-badge { cursor: pointer; }
.admin-name { font-size: 14px; color: #64748b; }

.app-main {
  background: #f1f5f9;
  padding: 24px;
  overflow-y: auto;
}
</style>
