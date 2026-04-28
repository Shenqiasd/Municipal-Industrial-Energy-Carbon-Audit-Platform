<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { queryExtractedTable } from '@/api/extracted-data'
import SectionTitle from '../components/SectionTitle.vue'
import RegulationTable from '../components/RegulationTable.vue'
import type { RegColumn } from '../components/RegulationTable.vue'

const loading = ref(false)
const summaryRows = ref<Record<string, unknown>[]>([])
const annualRows = ref<Record<string, unknown>[]>([])
const tableError = ref('')

const summaryColumns: RegColumn[] = [
  { prop: 'itemName', label: '项目', minWidth: 200 },
  { prop: 'actual2025', label: '2025年实际', minWidth: 120 },
  { prop: 'target2030', label: '2030年目标', minWidth: 120 },
  { prop: 'changeRate', label: '增减（%）', minWidth: 100 },
]

const annualColumns: RegColumn[] = [
  { prop: 'itemName', label: '项目', minWidth: 200 },
  { prop: 'y2026', label: '2026年', minWidth: 100 },
  { prop: 'y2027', label: '2027年', minWidth: 100 },
  { prop: 'y2028', label: '2028年', minWidth: 100 },
  { prop: 'y2029', label: '2029年', minWidth: 100 },
  { prop: 'y2030', label: '2030年', minWidth: 100 },
]

onMounted(async () => {
  loading.value = true
  try {
    const data = await queryExtractedTable('de_five_year_target', { pageSize: 200 }).catch((e: Error) => {
      tableError.value = e.message?.includes('404') ? '数据表尚未对接' : ''
      return { rows: [], total: 0 }
    })
    const allRows = data.rows || []
    summaryRows.value = allRows.filter((r: Record<string, unknown>) => r.rowType === 'summary' || r.actual2025 !== undefined)
    annualRows.value = allRows.filter((r: Record<string, unknown>) => r.rowType === 'annual' || r.y2026 !== undefined)
    if (!summaryRows.value.length && !annualRows.value.length) {
      summaryRows.value = allRows
    }
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div>
    <SectionTitle title="表18：十五五期间节能目标" />
    <el-alert v-if="tableError" :title="tableError" type="warning" show-icon :closable="false" style="margin-bottom: 12px" />

    <RegulationTable
      :columns="summaryColumns"
      :data="summaryRows"
      :loading="loading"
      export-filename="十五五节能目标-总览"
      title="2025年实际 vs 2030年目标"
    />

    <RegulationTable
      v-if="annualRows.length"
      :columns="annualColumns"
      :data="annualRows"
      :loading="loading"
      export-filename="十五五节能目标-年度"
      title="年度目标进度（2026-2030年）"
    />
  </div>
</template>
