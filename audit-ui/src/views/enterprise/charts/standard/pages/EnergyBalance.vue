<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { queryExtractedTable } from '@/api/extracted-data'
import SectionTitle from '../components/SectionTitle.vue'
import RegulationTable from '../components/RegulationTable.vue'
import type { RegColumn } from '../components/RegulationTable.vue'

const loading = ref(false)
const rows = ref<Record<string, unknown>[]>([])
const tableError = ref('')

const columns: RegColumn[] = [
  { prop: 'itemName', label: '项目', minWidth: 180 },
  { prop: 'electricity', label: '电力（万kWh）', minWidth: 120 },
  { prop: 'coal', label: '煤炭（吨）', minWidth: 100 },
  { prop: 'naturalGas', label: '天然气（万m³）', minWidth: 120 },
  { prop: 'steam', label: '蒸汽（GJ）', minWidth: 100 },
  { prop: 'diesel', label: '柴油（吨）', minWidth: 100 },
  { prop: 'gasoline', label: '汽油（吨）', minWidth: 100 },
  { prop: 'other', label: '其他', minWidth: 100 },
  { prop: 'totalTce', label: '合计（tce）', minWidth: 120 },
]

onMounted(async () => {
  loading.value = true
  try {
    const data = await queryExtractedTable('de_energy_balance', { pageSize: 200 }).catch((e: Error) => {
      tableError.value = e.message?.includes('404') ? '数据表尚未对接' : ''
      return { rows: [], total: 0 }
    })
    rows.value = data.rows || []
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div>
    <SectionTitle title="表9：能源消费平衡综合表" />
    <el-alert v-if="tableError" :title="tableError" type="warning" show-icon :closable="false" style="margin-bottom: 12px" />
    <RegulationTable
      :columns="columns"
      :data="rows"
      :loading="loading"
      export-filename="能源消费平衡综合表"
      title="能源消费平衡综合表"
    />
  </div>
</template>
