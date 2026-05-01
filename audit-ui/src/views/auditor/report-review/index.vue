<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Download } from '@element-plus/icons-vue'
import {
  listReportsForReview,
  getReportForReview,
  approveReport,
  rejectReport,
  downloadUploadedReport,
  REPORT_STATUS_MAP,
  type ArReport,
} from '@/api/report'

const loading = ref(false)
const reports = ref<ArReport[]>([])
const filterStatus = ref<number | undefined>(undefined)
const filterYear = ref<number | undefined>(undefined)

const reviewDialogVisible = ref(false)
const currentReport = ref<ArReport | null>(null)
const reviewComment = ref('')
const actionLoading = ref(false)

const detailDialogVisible = ref(false)
const detailReport = ref<ArReport | null>(null)
const detailLoading = ref(false)
const detailDownloading = ref(false)

const downloadingId = ref<number | null>(null)

// `undefined` sentinels represent "no filter"; they're cast so el-option's
// strict value typing accepts them. The select is `clearable`, which resets
// the v-model to undefined at runtime.
const ALL: number = undefined as unknown as number

const statusOptions: { value: number; label: string }[] = [
  { value: ALL, label: '全部状态' },
  { value: 4, label: '待审核' },
  { value: 5, label: '已通过' },
  { value: 6, label: '已退回' },
]

const currentYear = new Date().getFullYear()
const yearOptions = computed(() => {
  const years: { value: number; label: string }[] = [{ value: ALL, label: '全部年度' }]
  for (let y = currentYear; y >= currentYear - 5; y--) {
    years.push({ value: y, label: `${y}年` })
  }
  return years
})

async function loadReports() {
  loading.value = true
  try {
    reports.value = await listReportsForReview(filterStatus.value, filterYear.value)
  } catch (e: unknown) {
    ElMessage.error('加载报告列表失败：' + extractMsg(e))
  } finally {
    loading.value = false
  }
}

function onFilterChange() {
  loadReports()
}

function statusTag(status: number | undefined) {
  if (status === undefined || status === null) return { label: '未知', type: 'info' as const }
  return REPORT_STATUS_MAP[status] ?? { label: '未知', type: 'info' as const }
}

function formatDate(dt: string | null | undefined) {
  if (!dt) return '—'
  return dt.substring(0, 19).replace('T', ' ')
}

function formatBytes(bytes: number | null | undefined): string {
  if (!bytes || bytes <= 0) return '—'
  const units = ['B', 'KB', 'MB', 'GB']
  let v = bytes
  let i = 0
  while (v >= 1024 && i < units.length - 1) {
    v /= 1024
    i += 1
  }
  return `${v.toFixed(v >= 100 || i === 0 ? 0 : 1)} ${units[i]}`
}

function hasUploadedFile(report: ArReport | null | undefined): boolean {
  if (!report) return false
  return !!report.uploadedFilePath || !!report.uploadedFileName
}

async function openDetail(report: ArReport) {
  detailLoading.value = true
  detailDialogVisible.value = true
  try {
    detailReport.value = await getReportForReview(report.id)
  } catch (e: unknown) {
    ElMessage.error('加载报告详情失败：' + extractMsg(e))
    detailDialogVisible.value = false
  } finally {
    detailLoading.value = false
  }
}

function openReviewDialog(report: ArReport) {
  currentReport.value = report
  reviewComment.value = ''
  reviewDialogVisible.value = true
}

async function handleApprove() {
  if (!currentReport.value) return
  try {
    await ElMessageBox.confirm('确认审核通过该报告？', '审核通过', { type: 'success' })
  } catch { return }
  actionLoading.value = true
  try {
    await approveReport(currentReport.value.id, reviewComment.value || undefined)
    ElMessage.success('报告已审核通过')
    reviewDialogVisible.value = false
    loadReports()
  } catch (e: unknown) {
    ElMessage.error('操作失败：' + extractMsg(e))
  } finally {
    actionLoading.value = false
  }
}

async function handleReject() {
  if (!currentReport.value) return
  if (!reviewComment.value.trim()) {
    ElMessage.warning('退回时必须填写退回理由')
    return
  }
  try {
    await ElMessageBox.confirm('确认退回该报告？企业将看到退回理由。', '退回报告', { type: 'warning' })
  } catch { return }
  actionLoading.value = true
  try {
    await rejectReport(currentReport.value.id, reviewComment.value)
    ElMessage.success('报告已退回')
    reviewDialogVisible.value = false
    loadReports()
  } catch (e: unknown) {
    ElMessage.error('操作失败：' + extractMsg(e))
  } finally {
    actionLoading.value = false
  }
}

function triggerBlobDownload(blob: Blob, filename: string) {
  const url = window.URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = filename
  document.body.appendChild(a)
  a.click()
  document.body.removeChild(a)
  window.URL.revokeObjectURL(url)
}

async function handleDownload(report: ArReport) {
  downloadingId.value = report.id
  try {
    const blob = await downloadUploadedReport(report.id)
    const fallback = (report.reportName || `audit-report-${report.id}`) + '.docx'
    triggerBlobDownload(blob as Blob, report.uploadedFileName || fallback)
  } catch (e: unknown) {
    ElMessage.error('下载失败：' + extractMsg(e))
  } finally {
    downloadingId.value = null
  }
}

async function handleDetailDownload() {
  if (!detailReport.value) return
  detailDownloading.value = true
  try {
    const blob = await downloadUploadedReport(detailReport.value.id)
    const fallback = (detailReport.value.reportName || `audit-report-${detailReport.value.id}`) + '.docx'
    triggerBlobDownload(blob as Blob, detailReport.value.uploadedFileName || fallback)
  } catch (e: unknown) {
    ElMessage.error('下载失败：' + extractMsg(e))
  } finally {
    detailDownloading.value = false
  }
}

function extractMsg(e: unknown): string {
  if (e instanceof Error) return e.message
  if (typeof e === 'string') return e
  return '未知错误'
}

onMounted(loadReports)
</script>

<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">报告审核</span>
          <div class="filters">
            <el-select v-model="filterStatus" placeholder="状态" @change="onFilterChange" clearable style="width: 130px">
              <el-option
                v-for="opt in statusOptions"
                :key="String(opt.value)"
                :value="opt.value"
                :label="opt.label"
              />
            </el-select>
            <el-select v-model="filterYear" placeholder="年度" @change="onFilterChange" clearable style="width: 110px; margin-left: 8px">
              <el-option
                v-for="opt in yearOptions"
                :key="String(opt.value)"
                :value="opt.value"
                :label="opt.label"
              />
            </el-select>
          </div>
        </div>
      </template>

      <el-table v-loading="loading" :data="reports" border stripe size="small" style="width: 100%">
        <el-table-column label="报告名称" prop="reportName" min-width="180" show-overflow-tooltip />
        <el-table-column label="企业名称" prop="enterpriseName" min-width="140" show-overflow-tooltip />
        <el-table-column label="审计年度" prop="auditYear" width="90" align="center" />
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.status).type" size="small">
              {{ statusTag(row.status).label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="上传文件" min-width="160" show-overflow-tooltip>
          <template #default="{ row }">
            <span v-if="row.uploadedFileName">{{ row.uploadedFileName }}</span>
            <span v-else style="color:#c0c4cc">—</span>
          </template>
        </el-table-column>
        <el-table-column label="文件大小" width="100" align="right">
          <template #default="{ row }">{{ formatBytes(row.uploadedFileSize) }}</template>
        </el-table-column>
        <el-table-column label="提交时间" width="160" align="center">
          <template #default="{ row }">{{ formatDate(row.submitTime) }}</template>
        </el-table-column>
        <el-table-column label="审核意见" prop="reviewComment" min-width="140" show-overflow-tooltip />
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="openDetail(row)">
              查看
            </el-button>
            <el-button
              v-if="row.status === 4"
              link
              type="success"
              size="small"
              @click="openReviewDialog(row)"
            >
              审核
            </el-button>
            <el-button
              v-if="hasUploadedFile(row)"
              link
              type="info"
              size="small"
              :loading="downloadingId === row.id"
              @click="handleDownload(row)"
            >
              下载报告
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="reports.length === 0 && !loading" style="color: #909399; text-align: center; padding: 40px">
        暂无报告数据
      </div>
    </el-card>

    <!-- Review Dialog -->
    <el-dialog v-model="reviewDialogVisible" title="报告审核" width="520px" :close-on-click-modal="false">
      <div v-if="currentReport" style="margin-bottom: 16px">
        <el-descriptions :column="1" border size="small">
          <el-descriptions-item label="报告名称">{{ currentReport.reportName }}</el-descriptions-item>
          <el-descriptions-item label="企业名称">{{ currentReport.enterpriseName || '—' }}</el-descriptions-item>
          <el-descriptions-item label="审计年度">{{ currentReport.auditYear }}</el-descriptions-item>
          <el-descriptions-item label="上传文件">
            <span v-if="currentReport.uploadedFileName">{{ currentReport.uploadedFileName }}</span>
            <span v-else style="color:#c0c4cc">—</span>
          </el-descriptions-item>
        </el-descriptions>
        <div v-if="hasUploadedFile(currentReport)" style="margin-top:12px">
          <el-button
            :icon="Download"
            :loading="downloadingId === currentReport.id"
            @click="handleDownload(currentReport)"
          >下载报告（.docx）</el-button>
          <span style="font-size:12px; color:#909399; margin-left:8px">
            建议先下载并阅读企业上传的报告内容后再做出审核决定
          </span>
        </div>
      </div>
      <el-input
        v-model="reviewComment"
        type="textarea"
        :rows="4"
        placeholder="请输入审核意见（退回时必填）..."
      />
      <template #footer>
        <el-button @click="reviewDialogVisible = false">取消</el-button>
        <el-button type="danger" :loading="actionLoading" @click="handleReject">退回</el-button>
        <el-button type="success" :loading="actionLoading" @click="handleApprove">通过</el-button>
      </template>
    </el-dialog>

    <!-- Detail Dialog -->
    <el-dialog v-model="detailDialogVisible" title="报告详情" width="640px" :close-on-click-modal="true">
      <div v-loading="detailLoading">
        <template v-if="detailReport">
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item label="报告名称" :span="2">
              {{ detailReport.reportName }}
            </el-descriptions-item>
            <el-descriptions-item label="企业名称">
              {{ detailReport.enterpriseName || '—' }}
            </el-descriptions-item>
            <el-descriptions-item label="审计年度">
              {{ detailReport.auditYear }}
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="statusTag(detailReport.status).type" size="small">
                {{ statusTag(detailReport.status).label }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="提交时间">
              {{ formatDate(detailReport.submitTime) }}
            </el-descriptions-item>
            <el-descriptions-item label="上传文件">
              <span v-if="detailReport.uploadedFileName">{{ detailReport.uploadedFileName }}</span>
              <span v-else style="color:#c0c4cc">—</span>
            </el-descriptions-item>
            <el-descriptions-item label="文件大小">
              {{ formatBytes(detailReport.uploadedFileSize) }}
            </el-descriptions-item>
            <el-descriptions-item label="上传时间" :span="2">
              {{ formatDate(detailReport.uploadedAt) }}
            </el-descriptions-item>
            <el-descriptions-item label="审核意见" :span="2">
              {{ detailReport.reviewComment || '—' }}
            </el-descriptions-item>
          </el-descriptions>

          <div class="detail-actions">
            <el-button
              v-if="hasUploadedFile(detailReport)"
              type="primary"
              :icon="Download"
              :loading="detailDownloading"
              @click="handleDetailDownload"
            >下载企业上传的报告（.docx）</el-button>
            <span v-else style="color:#909399; font-size:13px">
              企业尚未上传报告文件
            </span>
          </div>
        </template>
      </div>
    </el-dialog>
  </div>
</template>

<style scoped>
.page-container {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.card-title {
  font-size: 16px;
  font-weight: 600;
}
.filters {
  display: flex;
  align-items: center;
}
.detail-actions {
  margin-top: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
}
</style>
