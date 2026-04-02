<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'

/**
 * SpreadDesigner — wraps GrapeCity SpreadJS Designer (loaded via CDN as window.GC).
 *
 * Props:
 *   templateJson — SpreadJS workbook JSON string to load on mount.
 *   readonly     — when true:
 *                  • Ribbon filtered to view-safe tabs only (via cloned config)
 *                  • All sheets are protected (isProtected = true)
 *                  • Mutating commands are overridden in commandManager
 *
 * Exposed:
 *   getJson(): string — serialises current workbook state to JSON.
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
  designer?.destroy?.()
  designer = null
  workbook = null
})

function initDesigner(gc: any) {
  if (!containerRef.value) return

  const GCDesigner = gc.Spread.Sheets.Designer
  const config = props.readonly ? buildReadonlyConfig(GCDesigner) : GCDesigner.DefaultConfig

  designer = new GCDesigner.Designer(containerRef.value, config, null)
  workbook = designer.getWorkbook()

  if (props.templateJson && props.templateJson !== '{}') {
    workbook.fromJSON(JSON.parse(props.templateJson))
  }

  if (props.readonly) {
    applySheetProtection()
    blockMutatingCommands()
  }
}

/** Strip non-view ribbon tabs so the designer cannot be used for edits. */
function buildReadonlyConfig(GCDesigner: any): any {
  const base = JSON.parse(JSON.stringify(GCDesigner.DefaultConfig ?? {}))
  const VIEW_TABS = new Set(['home', 'view'])
  if (Array.isArray(base?.ribbon)) {
    base.ribbon = base.ribbon.filter((tab: any) => VIEW_TABS.has((tab.id ?? '').toLowerCase()))
  }
  if (Array.isArray(base?.fileMenu?.menuItems)) {
    const ALLOWED_FILE = new Set(['open', 'close'])
    base.fileMenu.menuItems = base.fileMenu.menuItems.filter((item: any) =>
      ALLOWED_FILE.has((item.commandName ?? '').toLowerCase())
    )
  }
  return base
}

/** Protect all sheets so individual cells cannot be edited. */
function applySheetProtection() {
  const count = workbook.getSheetCount()
  for (let i = 0; i < count; i++) {
    workbook.getSheet(i).options.isProtected = true
  }
}

/**
 * Override mutating commands in the workbook's commandManager so any ribbon or
 * keyboard shortcut that slips through the filtered config is also blocked.
 */
const MUTATING_COMMANDS = [
  'clear', 'clearContents', 'clearFormat', 'clearAll',
  'delete', 'insertRows', 'insertColumns', 'deleteRows', 'deleteColumns',
  'insertSheet', 'deleteSheet', 'editCell', 'commitEdit',
  'paste', 'cut', 'redo', 'undo', 'sort', 'filter',
]

function blockMutatingCommands() {
  const commandManager = workbook.commandManager?.()
  if (!commandManager) return
  const noOp = { execute: () => false, canUndo: false }
  MUTATING_COMMANDS.forEach((name) => commandManager.register(name, noOp))
}

function getJson(): string {
  if (!workbook) return '{}'
  return JSON.stringify(workbook.toJSON())
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
