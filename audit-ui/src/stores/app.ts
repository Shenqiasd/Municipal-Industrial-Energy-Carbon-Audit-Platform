import { defineStore } from 'pinia'
import { ref } from 'vue'

export type DeviceType = 'desktop' | 'mobile'

export const useAppStore = defineStore('app', () => {
  const sidebarCollapsed = ref(false)
  const device = ref<DeviceType>('desktop')

  function toggleSidebar() {
    sidebarCollapsed.value = !sidebarCollapsed.value
  }

  function closeSidebar() {
    sidebarCollapsed.value = true
  }

  function setDevice(val: DeviceType) {
    device.value = val
  }

  return {
    sidebarCollapsed,
    device,
    toggleSidebar,
    closeSidebar,
    setDevice,
  }
})
