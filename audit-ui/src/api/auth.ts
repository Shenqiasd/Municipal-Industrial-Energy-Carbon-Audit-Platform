import request from '@/utils/request'

export interface LoginForm {
  username: string
  password: string
  portal: 'enterprise' | 'admin' | 'auditor'
}

export interface LoginResult {
  token: string
}

export interface UserInfoResult {
  user: {
    id: number
    username: string
    realName: string
    role: string
    portal: string
    enterpriseId?: number
    enterpriseName?: string
  }
  permissions: string[]
}

/** User login */
export function login(data: LoginForm) {
  return request.post<LoginResult>('/auth/login', data)
}

/** User logout */
export function logout() {
  return request.post('/auth/logout')
}

/** Get current user info */
export function getUserInfo() {
  return request.get<UserInfoResult>('/auth/info')
}
