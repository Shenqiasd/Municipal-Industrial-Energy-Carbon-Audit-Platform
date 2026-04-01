import request from '@/utils/request'

export interface Template {
  id: number
  name: string
  description: string
  version: string
  status: number
  createdAt: string
  updatedAt: string
}

export interface TemplateQuery {
  page?: number
  size?: number
  name?: string
  status?: number
}

export interface SubmissionData {
  templateId: number
  enterpriseId: number
  auditYear: number
  data: Record<string, unknown>
}

/** Get template list */
export function getList(params?: TemplateQuery) {
  return request.get('/template', { params })
}

/** Get template by ID */
export function getById(id: number) {
  return request.get(`/template/${id}`)
}

/** Create template */
export function create(data: Partial<Template>) {
  return request.post('/template', data)
}

/** Update template */
export function update(id: number, data: Partial<Template>) {
  return request.put(`/template/${id}`, data)
}

/** Publish template */
export function publish(id: number) {
  return request.put(`/template/${id}/publish`)
}

/** Get submission for a template */
export function getSubmission(templateId: number, params?: Record<string, unknown>) {
  return request.get(`/template/${templateId}/submission`, { params })
}

/** Save submission data */
export function saveSubmission(templateId: number, data: SubmissionData) {
  return request.post(`/template/${templateId}/submission`, data)
}
