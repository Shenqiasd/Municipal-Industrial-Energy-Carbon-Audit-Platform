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
  { prop: 'seqNo', label: '序号', width: 60 },
  { prop: 'equipmentType', label: '设备类型', minWidth: 110 },
  { prop: 'model', label: '型号', minWidth: 100 },
  { prop: 'purpose', label: '设备用途', minWidth: 120 },
  { prop: 'quantity', label: '台数', width: 70 },
  { prop: 'ratedParams', label: '额定参数', minWidth: 120 },
  { prop: 'measuredValue', label: '实测值', minWidth: 100 },
  { prop: 'efficiencyGrade', label: '能效等级', minWidth: 80 },
  { prop: 'benchmark', label: '对标基准', minWidth: 100 },
  { prop: 'isCompliant', label: '是否达标', width: 80 },
  { prop: 'remark', label: '备注', minWidth: 100 },
]

onMounted(async () => {
  loading.value = true
  try {
    const data = await queryExtractedTable('de_equipment_benchmark', { pageSize: 500 }).catch((e: Error) => {
      tableError.value = e.message?.includes('404') ? '数据表尚未对接' : ''
      return { rows: [], total: 0 }
    })
    rows.value = (data.rows || []).map((r, i) => ({ ...r, seqNo: i + 1 }))
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div>
    <SectionTitle title="表10：重点用能设备能效对标表" />
    <el-alert v-if="tableError" :title="tableError" type="warning" show-icon :closable="false" style="margin-bottom: 12px" />
    <RegulationTable
      :columns="columns"
      :data="rows"
      :loading="loading"
      export-filename="重点用能设备能效对标表"
      title="重点用能设备能效对标表"
    />
  </div>
</template>
