import request from '@/utils/request'

export interface Enterprise {
  id: number
  name: string
  creditCode: string
  industry: string
  address: string
  contact: string
  phone: string
  status: number
  createdAt: string
  updatedAt: string
}

export interface EnterpriseQuery {
  page?: number
  size?: number
  name?: string
  status?: number
}

/** Get enterprise list */
export function getList(params?: EnterpriseQuery) {
  return request.get('/enterprise', { params })
}

/** Get enterprise by ID */
export function getById(id: number) {
  return request.get(`/enterprise/${id}`)
}

/** Create enterprise */
export function create(data: Partial<Enterprise>) {
  return request.post('/enterprise', data)
}

/** Update enterprise */
export function update(id: number, data: Partial<Enterprise>) {
  return request.put(`/enterprise/${id}`, data)
}

/** Remove enterprise */
export function remove(id: number) {
  return request.delete(`/enterprise/${id}`)
}
