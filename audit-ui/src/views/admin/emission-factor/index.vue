<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Search, Refresh } from '@element-plus/icons-vue'
import {
  listEmissionFactor,
  createEmissionFactor,
  updateEmissionFactor,
  deleteEmissionFactor,
  type EmissionFactor,
} from '@/api/admin'

const loading = ref(false)
const tableData = ref<EmissionFactor[]>([])
const total = ref(0)

const query = ref({
  factorName: '',
  energyType: '',
  effectiveYear: undefined as number | undefined,
  status: undefined as number | undefined,
  pageNum: 1,
  pageSize: 15,
})

const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitting = ref(false)

const formRef = ref()
const form = ref<Partial<EmissionFactor>>({})
const rules = {
  factorName: [{ required: true, message: '请输入因子名称', trigger: 'blur' }],
  factorValue: [{ required: true, message: '请输入因子值', trigger: 'blur' }],
}

async function loadData() {
  loading.value = true
  try {
    const res = await listEmissionFactor(query.value)
    tableData.value = res.rows ?? []
    total.value = Number(res.total ?? 0)
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  query.value.pageNum = 1
  loadData()
}

function handleReset() {
  query.value = { factorName: '', energyType: '', effectiveYear: undefined, status: undefined, pageNum: 1, pageSize: 15 }
  loadData()
}

function openCreate() {
  form.value = { status: 1, effectiveYear: new Date().getFullYear() }
  dialogTitle.value = '新增排放因子'
  dialogVisible.value = true
}

function openEdit(row: EmissionFactor) {
  form.value = { ...row }
  dialogTitle.value = '编辑排放因子'
  dialogVisible.value = true
}

function handleClose() {
  formRef.value?.resetFields()
  dialogVisible.value = false
}

async function handleSubmit() {
  await formRef.value.validate()
  submitting.value = true
  try {
    if (form.value.id) {
      await updateEmissionFactor(form.value.id, form.value)
    } else {
      await createEmissionFactor(form.value)
    }
    ElMessage.success('保存成功')
    handleClose()
    loadData()
  } finally {
    submitting.value = false
  }
}

async function handleDelete(row: EmissionFactor) {
  await ElMessageBox.confirm(`确认删除排放因子「${row.factorName}」？`, '删除确认', { type: 'warning' })
  await deleteEmissionFactor(row.id!)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>

<template>
  <div class="page-container">
    <div class="toolbar">
      <el-form :model="query" inline>
        <el-form-item label="因子名称">
          <el-input v-model="query.factorName" placeholder="请输入名称" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="能源类型">
          <el-input v-model="query.energyType" placeholder="请输入类型" clearable style="width:140px" />
        </el-form-item>
        <el-form-item label="年份">
          <el-input-number v-model="query.effectiveYear" placeholder="年份" :controls="false" style="width:100px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable style="width:90px">
            <el-option label="启用" :value="1" />
            <el-option label="停用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <el-button type="primary" :icon="Plus" @click="openCreate">新增</el-button>
    </div>

    <el-table v-loading="loading" :data="tableData" border stripe>
      <el-table-column prop="factorName" label="因子名称" min-width="160" />
      <el-table-column prop="energyType" label="能源类型" width="120" />
      <el-table-column prop="factorValue" label="因子值" width="110" />
      <el-table-column prop="measurementUnit" label="单位" width="100" />
      <el-table-column prop="effectiveYear" label="有效年份" width="90" />
      <el-table-column prop="source" label="来源" min-width="140" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '启用' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="140" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" :icon="Edit" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="query.pageNum"
      v-model:page-size="query.pageSize"
      :total="total"
      :page-sizes="[10, 15, 30, 50]"
      layout="total, sizes, prev, pager, next"
      style="margin-top:16px"
      @change="loadData"
    />

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="560px" @close="handleClose">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="因子名称" prop="factorName">
          <el-input v-model="form.factorName" placeholder="请输入排放因子名称" />
        </el-form-item>
        <el-form-item label="能源类型">
          <el-input v-model="form.energyType" placeholder="如：煤炭、天然气" />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="因子值" prop="factorValue">
              <el-input-number v-model="form.factorValue" :precision="6" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计量单位">
              <el-input v-model="form.measurementUnit" placeholder="如：tCO₂/tce" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="有效年份">
          <el-input-number v-model="form.effectiveYear" :min="2000" :max="2100" :controls="false" style="width:120px" />
        </el-form-item>
        <el-form-item label="来源">
          <el-input v-model="form.source" placeholder="如：国家发改委2023年" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.page-container {
  padding: 20px;
}
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 16px;
}
</style>
