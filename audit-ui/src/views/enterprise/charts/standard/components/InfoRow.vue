<script setup lang="ts">
defineProps<{
  items: Array<{ label: string; value: unknown; span?: number }>
}>()
</script>

<template>
  <div class="info-row">
    <div
      v-for="(item, idx) in items"
      :key="idx"
      class="info-cell"
      :style="{ flex: item.span || 1 }"
    >
      <span class="info-label">{{ item.label }}</span>
      <span class="info-value">{{ item.value ?? '—' }}</span>
    </div>
  </div>
</template>

<style scoped lang="scss">
.info-row {
  display: flex;
  border-bottom: 1px solid #e8e8e8;

  &:first-child {
    border-top: 1px solid #e8e8e8;
  }
}

.info-cell {
  display: flex;
  min-height: 40px;
  line-height: 40px;
  border-right: 1px solid #e8e8e8;

  &:first-child {
    border-left: 1px solid #e8e8e8;
  }
}

.info-label {
  background: #f5f5f5;
  color: #595959;
  font-weight: 500;
  padding: 0 12px;
  white-space: nowrap;
  min-width: 140px;
  border-right: 1px solid #e8e8e8;
}

.info-value {
  padding: 0 12px;
  flex: 1;
  color: #303133;
}
</style>
