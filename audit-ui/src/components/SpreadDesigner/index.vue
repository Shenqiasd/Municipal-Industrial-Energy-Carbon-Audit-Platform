<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'

/**
 * SpreadDesigner — wraps GrapeCity SpreadJS Designer (loaded via CDN as window.GC).
 *
 * Props:
 *   templateJson — SpreadJS workbook JSON string to load on mount.
 *   readonly     — when true, all sheets are protected (no editing).
 *
 * Exposed methods:
 *   getJson(): string — serialises the current workbook state to JSON.
 */

const props = defineProps<{
  templateJson?: string
  readonly?: boolean
}>()

const containerRef = ref<HTMLDivElement>()
const gcError = ref(false)

let designer: any = null
let workbook: any = null

onMounted(() => {
  const gc = (window as any).GC
  if (!gc?.Spread?.Sheets?.Designer?.Designer) {
    gcError.value = true
    console.error('SpreadDesigner: GC.Spread.Sheets.Designer not found — check CDN scripts in index.html')
    return
  }
  initDesigner(gc)
})

onBeforeUnmount(() => {
  try {
    if (designer) {
      designer.destroy?.()
    }
  } catch (_) {}
  designer = null
  workbook = null
})

function initDesigner(gc: any) {
  if (!containerRef.value) return

  const config = gc.Spread.Sheets.Designer.DefaultConfig
  designer = new gc.Spread.Sheets.Designer.Designer(containerRef.value, config, null)
  workbook = designer.getWorkbook()

  if (props.templateJson && props.templateJson !== '{}') {
    try {
      workbook.fromJSON(JSON.parse(props.templateJson))
    } catch (e) {
      console.warn('SpreadDesigner: failed to parse templateJson —', e)
    }
  }

  if (props.readonly) {
    applyReadOnly()
  }
}

function applyReadOnly() {
  if (!workbook) return
  const count = workbook.getSheetCount()
  for (let i = 0; i < count; i++) {
    workbook.getSheet(i).options.isProtected = true
  }
}

function getJson(): string {
  if (!workbook) return '{}'
  try {
    return JSON.stringify(workbook.toJSON())
  } catch (e) {
    console.error('SpreadDesigner.getJson failed', e)
    return '{}'
  }
}

defineExpose({ getJson })
</script>

<template>
  <div class="spread-designer-wrapper">
    <el-alert
      v-if="gcError"
      type="error"
      title="SpreadJS 设计器加载失败"
      description="无法连接 CDN 加载 SpreadJS，请检查网络连接或刷新页面重试。"
      :closable="false"
      style="margin-bottom: 8px"
    />
    <div ref="containerRef" class="spread-designer-host" />
  </div>
</template>

<style scoped>
.spread-designer-wrapper {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.spread-designer-host {
  flex: 1;
  min-height: 0;
  width: 100%;
}
</style>
