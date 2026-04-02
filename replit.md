# Energy Audit Platform (能源审计平台)

## Project Overview

A comprehensive enterprise-level web application for managing energy consumption data, performing energy audits, and generating regulatory reports for industrial enterprises. Targets three user types: Enterprises, Administrators, and Auditors.

## Architecture

### Frontend (Vue 3 + Vite)
- Located in `audit-ui/`
- Vue 3 + TypeScript + Vite
- UI Library: Element Plus
- State Management: Pinia
- Three portals: admin, auditor, enterprise

### Backend (Spring Boot 3 - planned)
- Maven multi-module Java project
- Modules: audit-common, audit-model, audit-dao, audit-service, audit-web
- Java 17, Spring Boot 3.2.5, MyBatis, JWT
- MySQL 8.0 database (SQL in `/sql/`)

## Development Setup

### Frontend
```bash
cd audit-ui && npm install
npm run dev  # runs on port 5000
```

### Workflow
- "Start application" workflow: `cd audit-ui && npm run dev` on port 5000

## Key Technologies
- **SpreadJS**: Excel-like data entry interface
- **AntV X6**: Energy flow diagram visualization
- **ECharts**: Data dashboards
- **OnlyOffice**: Online document editing

## Deployment
- Static deployment: builds `audit-ui/dist`
- Build command: `cd audit-ui && npm run build`
