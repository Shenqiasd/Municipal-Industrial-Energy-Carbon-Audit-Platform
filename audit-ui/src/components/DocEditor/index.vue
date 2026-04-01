<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'

/**
 * DocEditor - OnlyOffice Document Editor Integration
 *
 * This component wraps OnlyOffice Document Editor for
 * viewing and editing audit report documents (DOCX format).
 *
 * The OnlyOffice Document Server needs to be running and
 * the API.js script needs to be loaded before initialization.
 *
 * Features:
 * - Full WYSIWYG document editing
 * - Collaborative editing support
 * - Document key based loading
 * - Readonly viewing mode
 *
 * Initialization flow:
 * 1. Load OnlyOffice API script if not already loaded
 * 2. Create DocsAPI.DocEditor instance on the container div
 * 3. Configure document key, permissions, and callbacks
 */

const props = defineProps<{
  documentKey: string
  readonly?: boolean
}>()

const editorRef = ref<HTMLDivElement>()

// OnlyOffice DocEditor instance
// let docEditor: any = null

onMounted(() => {
  initEditor()
})

onBeforeUnmount(() => {
  destroyEditor()
})

function initEditor() {
  if (!editorRef.value) return

  // TODO: Initialize OnlyOffice DocEditor
  // docEditor = new DocsAPI.DocEditor(editorRef.value.id, {
  //   document: {
  //     key: props.documentKey,
  //     fileType: 'docx',
  //     ...
  //   },
  //   editorConfig: {
  //     mode: props.readonly ? 'view' : 'edit',
  //     ...
  //   },
  // })
  console.log('OnlyOffice init placeholder', props.documentKey, props.readonly)
}

function destroyEditor() {
  // TODO: Destroy OnlyOffice editor instance
  // docEditor?.destroyEditor()
  // docEditor = null
}
</script>

<template>
  <div class="doc-editor-container">
    <div ref="editorRef" id="onlyoffice-editor" class="onlyoffice-host"></div>
  </div>
</template>

<style scoped lang="scss">
.doc-editor-container {
  width: 100%;
  height: 100%;
}

.onlyoffice-host {
  width: 100%;
  height: 700px;
  border: 1px solid #e4e7ed;
}
</style>
