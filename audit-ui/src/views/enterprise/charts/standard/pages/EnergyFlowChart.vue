<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import EnergyFlowDiagram4Stage from '@/components/EnergyFlowDiagram4Stage/index.vue'
import { getEnergyFlowList } from '@/api/energyFlow'
import type { EnergyFlowItem } from '@/api/energyFlow'
import { getUnitList, getEnergyList } from '@/api/setting'
import type { BsUnit, BsEnergy } from '@/api/setting'

const currentYear = new Date().getFullYear()
const auditYear = ref(currentYear)
const flowData = ref<EnergyFlowItem[]>([])
const units = ref<BsUnit[]>([])
const energies = ref<BsEnergy[]>([])
const loading = ref(false)
const diagramRef = ref<InstanceType<typeof EnergyFlowDiagram4Stage>>()

async function loadData() {
  loading.value = true
  try {
    const [flows, unitRes, energyRes] = await Promise.all([
      getEnergyFlowList(auditYear.value).catch(() => [] as EnergyFlowItem[]),
      getUnitList({ pageSize: 500 }).catch(() => ({ rows: [] as BsUnit[], total: 0 })),
      getEnergyList({ pageSize: 500 }).catch(() => ({ rows: [] as BsEnergy[], total: 0 })),
    ])
    flowData.value = flows
    units.value = unitRes.rows || []
    energies.value = energyRes.rows || []
  } catch {
    flowData.value = []
    units.value = []
    energies.value = []
  } finally {
    loading.value = false
  }
}

async function handleExportPng() {
  if (!diagramRef.value) return
  try {
    const dataUri = await diagramRef.value.exportPng()
    if (!dataUri) {
      ElMessage.warning('导出失败，图表为空')
      return
    }
    const link = document.createElement('a')
    link.download = `energy-flow-${auditYear.value}.png`
    link.href = dataUri
    link.click()
    ElMessage.success('已导出')
  } catch {
    ElMessage.error('导出失败')
  }
}

onMounted(() => {
  loadData()
})
</script>

<template>
  <div class="energy-flow-page">
    <div class="page-header">
      <h3>表5：能源流程图</h3>
      <div class="header-actions">
        <el-button @click="loadData" :loading="loading" size="small">刷新</el-button>
        <el-button @click="diagramRef?.fitView()" size="small">适应画布</el-button>
        <el-button type="primary" @click="handleExportPng" size="small">导出 PNG</el-button>
      </div>
    </div>
    <div class="flow-wrapper" v-loading="loading">
      <EnergyFlowDiagram4Stage
        ref="diagramRef"
        :flows="flowData"
        :units="units"
        :energies="energies"
      />
    </div>
  </div>
</template>

<style scoped lang="scss">
.energy-flow-page {
  height: calc(100vh - 200px);
  display: flex;
  flex-direction: column;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;

  h3 { margin: 0; font-size: 16px; color: #303133; }
}

.header-actions {
  display: flex;
  gap: 8px;
}

.flow-wrapper {
  flex: 1;
  min-height: 0;
  border: 1px solid #e6e6e6;
  border-radius: 4px;
}
</style>
