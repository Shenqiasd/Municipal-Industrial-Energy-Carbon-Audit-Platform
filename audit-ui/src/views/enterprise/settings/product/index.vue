<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Search, Refresh } from '@element-plus/icons-vue'
import {
  getProductList,
  createProduct,
  updateProduct,
  removeProduct,
  type BsProduct,
} from '@/api/setting'

const loading = ref(false)
const tableData = ref<BsProduct[]>([])
const total = ref(0)

const query = ref({ name: '', pageNum: 1, pageSize: 15 })

const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitting = ref(false)
const formRef = ref()
const form = ref<Partial<BsProduct>>({})
const rules = {
  name: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
}

async function loadData() {
  loading.value = true
  try {
    const res = await getProductList(query.value)
    tableData.value = res.rows ?? []
    total.value = Number(res.total ?? 0)
  } finally {
    loading.value = false
  }
}

function handleSearch() { query.value.pageNum = 1; loadData() }
function handleReset() { query.value = { name: '', pageNum: 1, pageSize: 15 }; loadData() }

function openCreate() {
  form.value = {}
  dialogTitle.value = '新增产品'
  dialogVisible.value = true
}

function openEdit(row: BsProduct) {
  form.value = { ...row }
  dialogTitle.value = '编辑产品'
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
      await updateProduct(form.value.id, form.value)
    } else {
      await createProduct(form.value)
    }
    ElMessage.success('保存成功')
    handleClose()
    loadData()
  } finally {
    submitting.value = false
  }
}

async function handleDelete(row: BsProduct) {
  await ElMessageBox.confirm(`确认删除产品「${row.name}」？`, '删除确认', { type: 'warning' })
  await removeProduct(row.id!)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>

<template>
  <div class="page-container">
    <div class="toolbar">
      <el-form :model="query" inline>
        <el-form-item label="产品名称">
          <el-input v-model="query.name" placeholder="请输入名称" clearable style="width:200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <el-button type="primary" :icon="Plus" @click="openCreate">新增</el-button>
    </div>

    <el-table v-loading="loading" :data="tableData" border stripe>
      <el-table-column prop="name" label="产品名称" min-width="180" />
      <el-table-column prop="measurementUnit" label="计量单位" width="110" />
      <el-table-column prop="unitPrice" label="单价(元)" width="110" />
      <el-table-column prop="remark" label="备注" min-width="160" show-overflow-tooltip />
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
      :page-sizes="[10, 15, 30]"
      layout="total, sizes, prev, pager, next"
      style="margin-top:16px"
      @change="loadData"
    />

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="480px" @close="handleClose">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="产品名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入产品名称" />
        </el-form-item>
        <el-form-item label="计量单位">
          <el-input v-model="form.measurementUnit" placeholder="如：吨、件、套" />
        </el-form-item>
        <el-form-item label="单价(元)">
          <el-input-number v-model="form.unitPrice" :precision="2" :min="0" style="width:100%" />
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
.page-container { padding: 20px; }
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 16px;
}
</style>
