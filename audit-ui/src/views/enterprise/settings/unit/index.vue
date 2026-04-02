<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Search, Refresh } from '@element-plus/icons-vue'
import {
  getUnitList,
  createUnit,
  updateUnit,
  removeUnit,
  getUnitEnergies,
  addUnitEnergy,
  removeUnitEnergy,
  getEnergyList,
  type BsUnit,
  type BsUnitEnergy,
  type BsEnergy,
} from '@/api/setting'

const loading = ref(false)
const tableData = ref<BsUnit[]>([])
const total = ref(0)

const query = ref({ name: '', pageNum: 1, pageSize: 15 })

const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitting = ref(false)
const formRef = ref()
const form = ref<Partial<BsUnit>>({})
const rules = {
  name: [{ required: true, message: '请输入单元名称', trigger: 'blur' }],
  unitType: [{ required: true, message: '请选择单元类型', trigger: 'change' }],
}

const UNIT_TYPE_OPTIONS = [
  { label: '主要生产单元', value: 1 },
  { label: '辅助生产单元', value: 2 },
  { label: '附属生产单元', value: 3 },
  { label: '非生产单元', value: 4 },
]

const energyDrawerVisible = ref(false)
const currentUnit = ref<BsUnit | null>(null)
const unitEnergies = ref<BsUnitEnergy[]>([])
const energyOptions = ref<BsEnergy[]>([])
const addingEnergy = ref(false)
const selectedAddEnergy = ref<number | null>(null)

async function loadData() {
  loading.value = true
  try {
    const res = await getUnitList(query.value)
    tableData.value = res.rows ?? []
    total.value = Number(res.total ?? 0)
  } finally {
    loading.value = false
  }
}

function handleSearch() { query.value.pageNum = 1; loadData() }
function handleReset() { query.value = { name: '', pageNum: 1, pageSize: 15 }; loadData() }

function openCreate() {
  form.value = { unitType: 1 }
  dialogTitle.value = '新增用能单元'
  dialogVisible.value = true
}

function openEdit(row: BsUnit) {
  form.value = { ...row }
  dialogTitle.value = '编辑用能单元'
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
      await updateUnit(form.value.id, form.value)
    } else {
      await createUnit(form.value)
    }
    ElMessage.success('保存成功')
    handleClose()
    loadData()
  } finally {
    submitting.value = false
  }
}

async function handleDelete(row: BsUnit) {
  await ElMessageBox.confirm(`确认删除用能单元「${row.name}」？`, '删除确认', { type: 'warning' })
  await removeUnit(row.id!)
  ElMessage.success('删除成功')
  loadData()
}

async function openEnergyDrawer(row: BsUnit) {
  currentUnit.value = row
  energyDrawerVisible.value = true
  selectedAddEnergy.value = null
  const [energies, myEnergies] = await Promise.all([
    getEnergyList({ pageSize: 200 }),
    getUnitEnergies(row.id!),
  ])
  energyOptions.value = energies.rows ?? []
  unitEnergies.value = myEnergies ?? []
}

async function handleAddEnergy() {
  if (!selectedAddEnergy.value || !currentUnit.value) return
  addingEnergy.value = true
  try {
    await addUnitEnergy(currentUnit.value.id!, selectedAddEnergy.value)
    ElMessage.success('关联成功')
    selectedAddEnergy.value = null
    unitEnergies.value = await getUnitEnergies(currentUnit.value.id!)
  } finally {
    addingEnergy.value = false
  }
}

async function handleRemoveEnergy(energyId: number) {
  if (!currentUnit.value) return
  await removeUnitEnergy(currentUnit.value.id!, energyId)
  ElMessage.success('已移除')
  unitEnergies.value = await getUnitEnergies(currentUnit.value.id!)
}

function getUnitTypeName(val: number) {
  return UNIT_TYPE_OPTIONS.find(o => o.value === val)?.label ?? val
}

onMounted(loadData)
</script>

<template>
  <div class="page-container">
    <div class="toolbar">
      <el-form :model="query" inline>
        <el-form-item label="单元名称">
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
      <el-table-column prop="name" label="单元名称" min-width="160" />
      <el-table-column prop="unitType" label="单元类型" width="140">
        <template #default="{ row }">{{ getUnitTypeName(row.unitType) }}</template>
      </el-table-column>
      <el-table-column prop="subCategory" label="子类别" width="130" />
      <el-table-column prop="remark" label="备注" min-width="160" show-overflow-tooltip />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEnergyDrawer(row)">能源关联</el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" @close="handleClose">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="单元名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入用能单元名称" />
        </el-form-item>
        <el-form-item label="单元类型" prop="unitType">
          <el-select v-model="form.unitType" style="width:100%">
            <el-option v-for="o in UNIT_TYPE_OPTIONS" :key="o.value" :label="o.label" :value="o.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="子类别">
          <el-input v-model="form.subCategory" placeholder="请输入子类别" />
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

    <el-drawer v-model="energyDrawerVisible" :title="`能源关联 — ${currentUnit?.name ?? ''}`" size="480px">
      <div class="drawer-body">
        <div class="add-energy-row">
          <el-select v-model="selectedAddEnergy" placeholder="选择能源品种" filterable clearable style="flex:1">
            <el-option
              v-for="e in energyOptions"
              :key="e.id"
              :label="e.name"
              :value="e.id"
              :disabled="unitEnergies.some(ue => ue.energyId === e.id)"
            />
          </el-select>
          <el-button type="primary" :loading="addingEnergy" @click="handleAddEnergy">关联</el-button>
        </div>
        <el-divider />
        <el-table :data="unitEnergies" border size="small">
          <el-table-column prop="energyName" label="能源名称" min-width="140" />
          <el-table-column prop="measurementUnit" label="计量单位" width="100" />
          <el-table-column label="操作" width="80">
            <template #default="{ row }">
              <el-button link type="danger" :icon="Delete" @click="handleRemoveEnergy(row.energyId)">移除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-if="!unitEnergies.length" description="暂未关联能源品种" />
      </div>
    </el-drawer>
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
.drawer-body { padding: 0 4px; }
.add-energy-row { display: flex; gap: 12px; align-items: center; }
</style>
