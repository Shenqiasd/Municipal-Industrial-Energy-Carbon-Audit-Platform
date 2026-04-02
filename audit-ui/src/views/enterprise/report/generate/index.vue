<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import {
  getTemplateList,
  listSubmissions,
  getPublishedVersion,
  submitSubmission,
  type TplTemplate,
  type TplSubmission,
} from '@/api/template'

const router = useRouter()
const loading = ref(false)
const submissions = ref<TplSubmission[]>([])
const templates = ref<TplTemplate[]>([])
const selectedYear = ref<number>(new Date().getFullYear())
const yearOptions = Array.from({ length: 6 }, (_, i) => new Date().getFullYear() - i)
const submittingId = ref<number | null>(null)

interface Row {
  template: TplTemplate
  submission: TplSubmission | null
  status: 0 | 1 | -1
}

const rows = computed<Row[]>(() => {
  const subMap = new Map<number, TplSubmission>()
  submissions.value
    .filter(s => s.auditYear === selectedYear.value)
    .forEach(s => { if (s.templateId) subMap.set(s.templateId, s) })

  return templates.value.map(t => {
    const sub = t.id != null ? subMap.get(t.id) ?? null : null
    const status: -1 | 0 | 1 = sub == null ? -1 : (sub.status as 0 | 1)
    return { template: t, submission: sub, status }
  })
})

const STATUS_MAP: Record<number, { label: string; type: 'info' | 'warning' | 'success' }> = {
  [-1]: { label: '未开始', type: 'info' },
  0: { label: '草稿', type: 'warning' },
  1: { label: '已提交', type: 'success' },
}

async function loadData() {
  loading.value = true
  try {
    const [subs, tpls] = await Promise.all([
      listSubmissions(),
      getTemplateList({ status: 1, pageSize: 200 }).then(r => r.rows ?? []),
    ])
    submissions.value = subs
    templates.value = tpls
  } finally {
    loading.value = false
  }
}

async function handleSubmit(row: Row) {
  if (!row.submission) {
    ElMessage.warning('尚未填报任何数据，请先前往填报页面保存草稿')
    return
  }
  try {
    await ElMessageBox.confirm(
      `确认提交「${row.template.templateName}」${selectedYear.value} 年度数据？提交后将触发数据抽取并锁定编辑。`,
      '提交确认',
      { type: 'warning' }
    )
  } catch {
    return
  }
  submittingId.value = row.submission.id!
  try {
    const publishedVer = await getPublishedVersion(row.template.id!)
    if (!publishedVer?.id) {
      ElMessage.error('模板尚未发布有效版本，无法提交')
      return
    }
    await submitSubmission(row.submission.id!, publishedVer.id)
    ElMessage.success('提交成功，数据已抽取')
    loadData()
  } catch (e: any) {
    ElMessage.error('提交失败：' + (e?.message ?? '未知错误'))
  } finally {
    submittingId.value = null
  }
}

function goToFill(row: Row) {
  router.push({
    path: '/enterprise/report/input',
    query: { templateId: row.template.id, year: selectedYear.value },
  })
}

function viewDetail(row: Row) {
  router.push({ path: '/enterprise/report/detail', query: { id: row.submission?.id } })
}

onMounted(loadData)
</script>

<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">填报进度概览</span>
          <div class="header-right">
            <el-select
              v-model="selectedYear"
              style="width:110px;margin-right:12px"
              size="small"
            >
              <el-option v-for="y in yearOptions" :key="y" :label="`${y}年`" :value="y" />
            </el-select>
            <el-button @click="loadData" :loading="loading" size="small">刷新</el-button>
          </div>
        </div>
      </template>

      <el-table v-loading="loading" :data="rows" border stripe>
        <el-table-column label="模板名称" min-width="180">
          <template #default="{ row }">{{ row.template.templateName }}</template>
        </el-table-column>
        <el-table-column label="当前版本" width="90" align="center">
          <template #default="{ row }">v{{ row.template.currentVersion ?? 1 }}</template>
        </el-table-column>
        <el-table-column label="年度" width="90" align="center">
          {{ selectedYear }} 年
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="STATUS_MAP[row.status]?.type" size="small">
              {{ STATUS_MAP[row.status]?.label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="提交时间" width="160">
          <template #default="{ row }">{{ row.submission?.submitTime ?? '—' }}</template>
        </el-table-column>
        <el-table-column label="最后保存" width="160">
          <template #default="{ row }">{{ row.submission?.updateTime ?? '—' }}</template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button
              link
              type="primary"
              :disabled="row.status === 1"
              @click="goToFill(row)"
            >
              去填报
            </el-button>
            <el-button
              link
              type="success"
              :loading="submittingId === row.submission?.id"
              :disabled="row.status !== 0"
              @click="handleSubmit(row)"
            >
              提交数据
            </el-button>
            <el-button
              link
              type="info"
              :disabled="!row.submission"
              @click="viewDetail(row)"
            >
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.page-container {
  padding: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.card-title {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
}

.header-right {
  display: flex;
  align-items: center;
}
</style>
