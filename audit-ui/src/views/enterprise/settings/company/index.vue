<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getEnterpriseSetting, upsertEnterpriseSetting, type EnterpriseSetting } from '@/api/enterpriseSetting'

const saving = ref(false)
const loading = ref(false)

const formRef = ref()
const form = ref<Partial<EnterpriseSetting>>({})

async function loadData() {
  loading.value = true
  try {
    const res = await getEnterpriseSetting()
    form.value = res ?? {}
  } finally {
    loading.value = false
  }
}

async function handleSave() {
  await formRef.value?.validate()
  saving.value = true
  try {
    await upsertEnterpriseSetting(form.value)
    ElMessage.success('保存成功')
  } finally {
    saving.value = false
  }
}

onMounted(loadData)
</script>

<template>
  <div class="page-container" v-loading="loading">
    <div class="page-header">
      <span class="title">3.1 企业基本信息</span>
      <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
    </div>

    <el-form ref="formRef" :model="form" label-width="140px" class="setting-form">

      <el-divider content-position="left">通讯信息</el-divider>
      <el-row :gutter="24">
        <el-col :span="12">
          <el-form-item label="企业地址">
            <el-input v-model="form.enterpriseAddress" placeholder="请输入企业地址" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单位地址">
            <el-input v-model="form.unitAddress" placeholder="请输入单位地址" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="邮政编码">
            <el-input v-model="form.postalCode" placeholder="请输入邮政编码" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="传真">
            <el-input v-model="form.fax" placeholder="请输入传真号码" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="left">法人代表</el-divider>
      <el-row :gutter="24">
        <el-col :span="12">
          <el-form-item label="法人代表姓名">
            <el-input v-model="form.legalRepresentative" placeholder="请输入法人代表姓名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="法人代表电话">
            <el-input v-model="form.legalPhone" placeholder="请输入法人代表电话" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="left">企业联系人</el-divider>
      <el-row :gutter="24">
        <el-col :span="12">
          <el-form-item label="企业联系人">
            <el-input v-model="form.enterpriseContact" placeholder="请输入联系人姓名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="企业联系手机">
            <el-input v-model="form.enterpriseMobile" placeholder="请输入手机号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="企业联系邮箱">
            <el-input v-model="form.enterpriseEmail" placeholder="请输入邮箱" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="left">编制人</el-divider>
      <el-row :gutter="24">
        <el-col :span="12">
          <el-form-item label="编制人联系人">
            <el-input v-model="form.compilerContact" placeholder="请输入联系人" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制人姓名">
            <el-input v-model="form.compilerName" placeholder="请输入姓名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制人手机">
            <el-input v-model="form.compilerMobile" placeholder="请输入手机号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制人邮箱">
            <el-input v-model="form.compilerEmail" placeholder="请输入邮箱" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="left">能源认证</el-divider>
      <el-row :gutter="24">
        <el-col :span="12">
          <el-form-item label="是否通过能源认证">
            <el-radio-group v-model="form.energyCert">
              <el-radio :value="1">是</el-radio>
              <el-radio :value="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="认证机构">
            <el-input v-model="form.certAuthority" placeholder="请输入认证机构名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="认证通过日期">
            <el-date-picker v-model="form.certPassDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width:100%" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="left">工商信息</el-divider>
      <el-row :gutter="24">
        <el-col :span="12">
          <el-form-item label="注册资本(万元)">
            <el-input-number v-model="form.registeredCapital" :min="0" :precision="2" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工商注册日期">
            <el-date-picker v-model="form.registeredDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="行业大类">
            <el-input v-model="form.industryCategory" placeholder="请输入行业大类" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="行业代码">
            <el-input v-model="form.industryCode" placeholder="请输入行业代码" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="行业小类名称">
            <el-input v-model="form.industryName" placeholder="请输入行业小类名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="上级主管部门">
            <el-input v-model="form.superiorDepartment" placeholder="请输入主管部门" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单位性质">
            <el-input v-model="form.unitNature" placeholder="如：国有、民营、外资" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="用能企业类型">
            <el-input v-model="form.energyEnterpriseType" placeholder="请输入类型" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="备注">
        <el-input v-model="form.remark" type="textarea" :rows="3" />
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped lang="scss">
.page-container {
  padding: 20px;
  max-width: 1000px;
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  .title {
    font-size: 18px;
    font-weight: 600;
    color: #303133;
  }
}
.setting-form {
  background: #fff;
  border-radius: 8px;
  padding: 24px 32px 8px;
}
</style>
