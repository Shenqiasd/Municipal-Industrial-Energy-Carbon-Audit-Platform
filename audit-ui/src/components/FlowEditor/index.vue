<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'

/**
 * FlowEditor - AntV X6 Energy Flow Diagram Editor
 *
 * This component wraps AntV X6 for creating and editing
 * energy flow diagrams (Sankey-style or custom node-edge diagrams).
 *
 * The @antv/x6 package needs to be installed before this
 * component becomes functional.
 *
 * Features:
 * - Drag-and-drop node creation for energy sources, consumers, etc.
 * - Edge connections representing energy flow with quantity labels
 * - Supports readonly mode for viewing saved diagrams
 * - Save/load diagram data via backend API
 *
 * Initialization flow:
 * 1. Create X6 Graph instance on the container div
 * 2. Register custom node/edge shapes
 * 3. Load existing diagram if enterpriseId and auditYear provided
 * 4. Enable/disable editing based on readonly prop
 */

const props = defineProps<{
  enterpriseId: number
  auditYear: number
  diagramType?: string
  readonly?: boolean
}>()

const emit = defineEmits<{
  save: [data: Record<string, unknown>]
}>()

const graphRef = ref<HTMLDivElement>()

// AntV X6 Graph instance will be stored here
// let graph: Graph | null = null

onMounted(() => {
  initGraph()
})

onBeforeUnmount(() => {
  destroyGraph()
})

function initGraph() {
  if (!graphRef.value) return

  // TODO: Initialize AntV X6 Graph
  // graph = new Graph({
  //   container: graphRef.value,
  //   width: graphRef.value.offsetWidth,
  //   height: graphRef.value.offsetHeight,
  //   ...graphConfig,
  // })
  console.log('X6 Graph init placeholder', props.enterpriseId, props.auditYear)
}

function destroyGraph() {
  // TODO: Dispose X6 Graph instance
  // graph?.dispose()
  // graph = null
}

function save() {
  // TODO: Export graph data and emit save event
  // const data = graph?.toJSON()
  const data = {}
  emit('save', data)
}

defineExpose({
  save,
})
</script>

<template>
  <div class="flow-editor-container">
    <div ref="graphRef" class="x6-graph-host"></div>
  </div>
</template>

<style scoped lang="scss">
.flow-editor-container {
  width: 100%;
  height: 100%;
}

.x6-graph-host {
  width: 100%;
  height: 600px;
  border: 1px solid #e4e7ed;
}
</style>
