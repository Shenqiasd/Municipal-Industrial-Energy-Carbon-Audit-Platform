import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as loginApi, logout as logoutApi, getUserInfo as getUserInfoApi } from '@/api/auth'
import type { LoginForm } from '@/api/auth'
import router from '@/router'

export interface UserInfo {
  id: number
  username: string
  realName: string
  role: string
  portal: string
  enterpriseId?: number
  enterpriseName?: string
}

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem('token') || '')
  const userInfo = ref<UserInfo | null>(null)
  const permissions = ref<string[]>([])

  async function login(form: LoginForm) {
    const res = await loginApi(form)
    token.value = res.data.token
    localStorage.setItem('token', res.data.token)
    return res
  }

  async function logout() {
    try {
      await logoutApi()
    } finally {
      resetState()
      router.push('/login')
    }
  }

  async function getUserInfo() {
    const res = await getUserInfoApi()
    userInfo.value = res.data.user
    permissions.value = res.data.permissions || []
    return res
  }

  function resetState() {
    token.value = ''
    userInfo.value = null
    permissions.value = []
    localStorage.removeItem('token')
  }

  return {
    token,
    userInfo,
    permissions,
    login,
    logout,
    getUserInfo,
    resetState,
  }
})
