<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'

/**
 * SpreadSheet - SpreadJS Integration Wrapper
 *
 * This component wraps GrapeCity SpreadJS (SpreadJS) for rendering
 * and editing Excel-like spreadsheet templates.
 *
 * SpreadJS will be initialized in the spreadRef container element.
 * The @grapecity/spread-sheets package needs to be installed and
 * configured before this component becomes functional.
 *
 * Initialization flow:
 * 1. Mount the SpreadJS workbook on the container div
 * 2. Load template by templateId from the backend
 * 3. Populate data if enterpriseId and auditYear are provided
 * 4. Set readonly mode if specified
 * 5. Bind save/extract events
 */

const props = defineProps<{
  templateId: number
  readonly?: boolean
  enterpriseId?: number
  auditYear?: number
}>()

const emit = defineEmits<{
  save: [data: Record<string, unknown>]
  dataExtracted: [data: Record<string, unknown>]
}>()

const spreadRef = ref<HTMLDivElement>()

// SpreadJS workbook instance will be stored here
// let workbook: GC.Spread.Sheets.Workbook | null = null

onMounted(() => {
  initSpreadJS()
})

onBeforeUnmount(() => {
  destroySpreadJS()
})

function initSpreadJS() {
  if (!spreadRef.value) return

  // TODO: Initialize SpreadJS workbook
  // workbook = new GC.Spread.Sheets.Workbook(spreadRef.value)
  // Load template and data based on props
  console.log('SpreadJS init placeholder', props.templateId, props.readonly)
}

function destroySpreadJS() {
  // TODO: Destroy SpreadJS workbook instance
  // workbook?.destroy()
  // workbook = null
}

function save() {
  // TODO: Extract data from SpreadJS and emit save event
  const data = {}
  emit('save', data)
}

function extractData() {
  // TODO: Extract structured data from the spreadsheet
  const data = {}
  emit('dataExtracted', data)
}

defineExpose({
  save,
  extractData,
})
</script>

<template>
  <div class="spreadsheet-container">
    <div ref="spreadRef" class="spreadjs-host"></div>
  </div>
</template>

<style scoped lang="scss">
.spreadsheet-container {
  width: 100%;
  height: 100%;
}

.spreadjs-host {
  width: 100%;
  height: 600px;
  border: 1px solid #e4e7ed;
}
</style>
