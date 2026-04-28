<script setup lang="ts">
defineProps<{ title: string }>()
</script>

<template>
  <div class="section-title">{{ title }}</div>
</template>

<style scoped lang="scss">
.section-title {
  background: #1890ff;
  color: #fff;
  font-size: 15px;
  font-weight: 600;
  padding: 8px 16px;
  margin: 16px 0 12px;
  border-radius: 4px;
}
</style>
