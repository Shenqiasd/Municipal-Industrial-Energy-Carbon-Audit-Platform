import * as XLSX from 'xlsx'

export interface ExportColumn {
  prop: string
  label: string
}

export function exportTableToExcel(
  columns: ExportColumn[],
  rows: Record<string, unknown>[],
  filename: string,
) {
  const header = columns.map((c) => c.label)
  const data = rows.map((row) => columns.map((c) => row[c.prop] ?? ''))
  const ws = XLSX.utils.aoa_to_sheet([header, ...data])
  const wb = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(wb, ws, filename.substring(0, 31))
  XLSX.writeFile(wb, `${filename}.xlsx`)
}
