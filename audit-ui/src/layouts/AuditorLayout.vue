<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useAppStore } from '@/stores/app'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const appStore = useAppStore()

const menuItems = computed(() => [
  {
    index: '/auditor/dashboard',
    title: '审计首页',
    icon: 'Odometer',
  },
  {
    index: '/auditor/tasks',
    title: '审计任务',
    icon: 'List',
  },
  {
    index: '/auditor/review',
    title: '审核详情',
    icon: 'View',
  },
])

const activeMenu = computed(() => route.path)

function handleMenuSelect(index: string) {
  if (index.startsWith('/')) {
    router.push(index)
  }
}

async function handleLogout() {
  await userStore.logout()
}
</script>

<template>
  <el-container class="layout-container">
    <el-aside :width="appStore.sidebarCollapsed ? '64px' : '220px'" class="layout-aside">
      <div class="logo">
        <h1 v-show="!appStore.sidebarCollapsed">审计工作台</h1>
      </div>
      <el-scrollbar>
        <el-menu
          :default-active="activeMenu"
          :collapse="appStore.sidebarCollapsed"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409eff"
          @select="handleMenuSelect"
        >
          <el-menu-item v-for="item in menuItems" :key="item.index" :index="item.index">
            <el-icon><component :is="item.icon" /></el-icon>
            <span>{{ item.title }}</span>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <el-container>
      <el-header class="layout-header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="appStore.toggleSidebar">
            <component :is="appStore.sidebarCollapsed ? 'Expand' : 'Fold'" />
          </el-icon>
        </div>
        <div class="header-right">
          <el-dropdown trigger="click">
            <span class="user-info">
              {{ userStore.userInfo?.realName || userStore.userInfo?.username || '审计员' }}
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped lang="scss">
.layout-container {
  height: 100vh;
}

.layout-aside {
  background-color: #304156;
  transition: width 0.3s;
  overflow: hidden;

  .logo {
    height: 50px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;

    h1 {
      font-size: 16px;
      margin: 0;
      white-space: nowrap;
    }
  }
}

.layout-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #e6e6e6;
  background: #fff;

  .collapse-btn {
    font-size: 20px;
    cursor: pointer;
  }

  .user-info {
    display: flex;
    align-items: center;
    cursor: pointer;
    gap: 4px;
  }
}

.layout-main {
  background: #f0f2f5;
}
</style>
