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
    index: '/enterprise/dashboard',
    title: '工作台',
    icon: 'Odometer',
  },
  {
    index: 'settings',
    title: '基础设置',
    icon: 'Setting',
    children: [
      { index: '/enterprise/settings/company', title: '3.1 企业信息' },
      { index: '/enterprise/settings/energy', title: '3.2 能源品种' },
      { index: '/enterprise/settings/unit', title: '3.3 用能单元' },
      { index: '/enterprise/settings/product', title: '3.4 产品信息' },
    ],
  },
  {
    index: 'entry',
    title: '数据填报',
    icon: 'EditPen',
    children: [
      { index: '/enterprise/entry/overview', title: '4.1 概况' },
      { index: '/enterprise/entry/indicators', title: '4.2 能效指标' },
      { index: '/enterprise/entry/projects', title: '4.3 项目信息' },
      { index: '/enterprise/entry/meters', title: '4.4 计量器具' },
      { index: '/enterprise/entry/meter-rate', title: '4.5 计量器具配备率' },
      { index: '/enterprise/entry/benchmark', title: '4.6 能耗基准' },
      { index: '/enterprise/entry/equipment-energy', title: '4.7 设备能耗' },
      { index: '/enterprise/entry/equipment-summary', title: '4.8 设备汇总' },
      { index: '/enterprise/entry/equipment-test', title: '4.9 设备检测' },
      { index: '/enterprise/entry/obsolete', title: '4.10 淘汰设备' },
      { index: '/enterprise/entry/energy-flow', title: '4.11 能流图' },
      { index: '/enterprise/entry/product-consumption', title: '4.12 产品能耗' },
      { index: '/enterprise/entry/product-cost', title: '4.13 产品成本' },
      { index: '/enterprise/entry/saving-calc', title: '4.14 节能量计算' },
      { index: '/enterprise/entry/ghg-emission', title: '4.15 温室气体排放' },
      { index: '/enterprise/entry/waste-heat', title: '4.16 余热余压' },
      { index: '/enterprise/entry/saving-potential', title: '4.17 节能潜力' },
      { index: '/enterprise/entry/management-policy', title: '4.18 管理制度' },
      { index: '/enterprise/entry/improvement', title: '4.19 整改建议' },
      { index: '/enterprise/entry/tech-reform', title: '4.20 技改方案' },
      { index: '/enterprise/entry/rectification', title: '4.21 整改落实' },
      { index: '/enterprise/entry/five-year-target', title: '4.22 五年目标' },
      { index: '/enterprise/entry/energy-ghg-source', title: '4.23 能源与温室气体源' },
      { index: '/enterprise/entry/energy-data-query', title: '4.24 能源数据查询' },
    ],
  },
  {
    index: 'charts',
    title: '图表分析',
    icon: 'DataAnalysis',
    children: [
      { index: '/enterprise/charts/standard', title: '5.1 标准图表' },
      { index: '/enterprise/charts/report-assist', title: '5.2 报告辅助' },
    ],
  },
  {
    index: 'report',
    title: '报告管理',
    icon: 'Document',
    children: [
      { index: '/enterprise/report/input', title: '6.1 报告录入' },
      { index: '/enterprise/report/generate', title: '6.2 报告生成' },
      { index: '/enterprise/report/upload', title: '6.3 报告上传' },
      { index: '/enterprise/report/detail', title: '6.4 报告详情' },
    ],
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
        <h1 v-show="!appStore.sidebarCollapsed">能源审计平台</h1>
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
          <template v-for="item in menuItems" :key="item.index">
            <el-sub-menu v-if="item.children" :index="item.index">
              <template #title>
                <el-icon><component :is="item.icon" /></el-icon>
                <span>{{ item.title }}</span>
              </template>
              <el-menu-item
                v-for="child in item.children"
                :key="child.index"
                :index="child.index"
              >
                {{ child.title }}
              </el-menu-item>
            </el-sub-menu>
            <el-menu-item v-else :index="item.index">
              <el-icon><component :is="item.icon" /></el-icon>
              <span>{{ item.title }}</span>
            </el-menu-item>
          </template>
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
              {{ userStore.userInfo?.realName || userStore.userInfo?.username || '用户' }}
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
