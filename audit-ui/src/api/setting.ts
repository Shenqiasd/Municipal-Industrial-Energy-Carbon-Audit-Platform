import request from '@/utils/request'

// --- Energy category ---

export interface EnergyCategory {
  id: number
  name: string
  code: string
  unit: string
  coefficient: number
  sortOrder: number
}

export function getEnergyList(params?: Record<string, unknown>) {
  return request.get('/setting/energy', { params })
}

export function createEnergy(data: Partial<EnergyCategory>) {
  return request.post('/setting/energy', data)
}

export function updateEnergy(id: number, data: Partial<EnergyCategory>) {
  return request.put(`/setting/energy/${id}`, data)
}

export function removeEnergy(id: number) {
  return request.delete(`/setting/energy/${id}`)
}

// --- Unit ---

export interface EnergyUnit {
  id: number
  name: string
  parentId: number | null
  enterpriseId: number
  sortOrder: number
}

export function getUnitList(params?: Record<string, unknown>) {
  return request.get('/setting/unit', { params })
}

export function createUnit(data: Partial<EnergyUnit>) {
  return request.post('/setting/unit', data)
}

export function updateUnit(id: number, data: Partial<EnergyUnit>) {
  return request.put(`/setting/unit/${id}`, data)
}

export function removeUnit(id: number) {
  return request.delete(`/setting/unit/${id}`)
}

// --- Product ---

export interface Product {
  id: number
  name: string
  unit: string
  enterpriseId: number
  sortOrder: number
}

export function getProductList(params?: Record<string, unknown>) {
  return request.get('/setting/product', { params })
}

export function createProduct(data: Partial<Product>) {
  return request.post('/setting/product', data)
}

export function updateProduct(id: number, data: Partial<Product>) {
  return request.put(`/setting/product/${id}`, data)
}

export function removeProduct(id: number) {
  return request.delete(`/setting/product/${id}`)
}
