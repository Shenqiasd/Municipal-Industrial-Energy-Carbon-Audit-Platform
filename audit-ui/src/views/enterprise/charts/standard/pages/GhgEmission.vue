<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { queryExtractedTable } from '@/api/extracted-data'
import SectionTitle from '../components/SectionTitle.vue'
import RegulationTable from '../components/RegulationTable.vue'
import type { RegColumn } from '../components/RegulationTable.vue'

const loading = ref(false)
const emissionRows = ref<Record<string, unknown>[]>([])
const tableError = ref('')

const columns: RegColumn[] = [
  { prop: 'energyName', label: '能源品种', minWidth: 120 },
  { prop: 'consumption', label: '消费量', minWidth: 100 },
  { prop: 'unit', label: '单位', width: 80 },
  { prop: 'lowerHeatingValue', label: '低位发热量', minWidth: 110 },
  { prop: 'carbonContent', label: '含碳量', minWidth: 100 },
  { prop: 'oxidationRate', label: '氧化率', minWidth: 80 },
  { prop: 'co2Emission', label: 'CO₂排放量（tCO₂）', minWidth: 150 },
]

onMounted(async () => {
  loading.value = true
  try {
    const data = await queryExtractedTable('de_carbon_emission', { pageSize: 200 }).catch((e: Error) => {
      tableError.value = e.message?.includes('404') ? '数据表尚未对接' : ''
      return { rows: [], total: 0 }
    })
    emissionRows.value = data.rows || []
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div>
    <SectionTitle title="表8：温室气体排放表" />
    <el-alert v-if="tableError" :title="tableError" type="warning" show-icon :closable="false" style="margin-bottom: 12px" />
    <RegulationTable
      :columns="columns"
      :data="emissionRows"
      :loading="loading"
      export-filename="温室气体排放表"
      title="温室气体排放表"
    />
  </div>
</template>
