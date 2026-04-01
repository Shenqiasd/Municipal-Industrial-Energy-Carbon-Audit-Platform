# 能源审计平台 - 架构设计文档

## 1. 技术架构

```
┌─────────────────────────────────────────────────────────┐
│                     前端 (Vue 3)                         │
│  Element Plus + SpreadJS + AntV X6 + OnlyOffice         │
│  Pinia + Vue Router + Axios + TypeScript                │
├─────────────────────────────────────────────────────────┤
│                   Nginx (反向代理)                        │
├─────────────────────────────────────────────────────────┤
│                  后端 (Spring Boot 3)                    │
│  ┌──────────┬──────────┬──────────┬──────────────────┐  │
│  │ audit-web│ audit-   │ audit-dao│ audit-common     │  │
│  │ REST API │ service  │ MyBatis  │ 通用工具          │  │
│  │ JWT认证   │ 业务逻辑  │ 数据访问  │ 异常/响应/枚举    │  │
│  │ Swagger  │ Ehcache3 │ PageHelper│                 │  │
│  └──────────┴──────────┴──────────┴──────────────────┘  │
│  audit-model: 实体类 / DTO / VO                         │
├─────────────────────────────────────────────────────────┤
│                   MySQL 8.0 (单实例)                     │
│           55张表 / 9个模块 / utf8mb4                      │
└─────────────────────────────────────────────────────────┘
```

## 2. Maven 模块结构

```
energy-audit-platform/          # Parent POM
├── audit-common/               # 通用工具: R响应体, 异常处理, 常量, 枚举, 安全工具
├── audit-model/                # 数据模型: BaseEntity, 55张表实体类, DTO, VO
├── audit-dao/                  # 数据访问: MyBatis Mapper接口 + XML映射
├── audit-service/              # 业务逻辑: Service接口 + 实现 + SpreadJS数据抽取 + 编辑锁
├── audit-web/                  # Web层: Controller + 配置 + JWT拦截器 + 启动类
├── audit-ui/                   # 前端: Vue 3 + TypeScript + Vite
└── sql/                        # 数据库脚本
    └── schema.sql              # 完整DDL (55张表 + 初始字典数据)
```

依赖关系: `web -> service -> dao -> model -> common`

## 3. 数据库模块划分 (55张表)

| 前缀 | 模块 | 表数 | 说明 |
|------|------|------|------|
| sys_ | 系统管理 | 9 | 用户/角色/菜单/字典/配置/日志 |
| ent_ | 企业管理 | 3 | 注册申请/企业主表/企业设置 |
| bs_  | 基本设置 | 4 | 能源品种/单元/单元能源关联/产品 |
| tpl_ | 模板管理 | 5 | 模板/版本/标签映射/填报数据/编辑锁 |
| de_  | 数据录入 | 25 | 4.1-4.24全部业务数据表 |
| ar_  | 审计报告 | 4 | 报告/章节/版本/附件 |
| aw_  | 审核流程 | 3 | 审核任务/日志/整改跟踪 |
| cm_  | 碳排放 | 1 | 碳排放因子 |
| ch_  | 图表 | 1 | 图表配置 |

## 4. 三端架构

### 4.1 企业端 (/enterprise/*)
- 基本设置: 企业信息(3.1) / 能源品种(3.2) / 用能单元(3.3) / 产品(3.4)
- 数据录入: 24个子模块(4.1-4.24), 混合使用SpreadJS表格和传统表单
- 能源流程图: AntV X6 可拖拽交互式编辑器
- 图表输出: 规定图表(5.1) + 报告辅助图表(5.2), 仅查询/导出
- 审计报告: 信息录入(6.1) + 自动生成(6.2) + OnlyOffice在线编辑 + 上传(6.3)

### 4.2 管理端 (/admin/*)
- 企业管理: 创建/锁定/解锁/过期管理
- 注册审核: 审核企业自主注册申请
- 模板管理: SpreadJS Designer在线设计 + Excel导入 + 版本发布
- 能源品类管理 / 碳排放因子管理
- 审核任务管理

### 4.3 审核端 (/auditor/*)
- 审核任务列表
- 单级审核: 企业提交 -> 审核 -> 通过/退回
- 整改跟踪: 未启动/进行中/已完成/超期预警

## 5. SpreadJS 集成架构

```
┌────────────────────┐    ┌──────────────────────┐
│   管理端模板设计      │    │   企业端数据填报        │
│                    │    │                      │
│ SpreadJS Designer  │    │  SpreadJS Workbook   │
│ + Excel导入        │    │  加载模板 + 填报数据    │
│                    │    │                      │
│ 定义 Tag/NamedRange│    │  保存 → 前端抽取Tag值  │
└────────┬───────────┘    └──────────┬───────────┘
         │                           │
         ▼                           ▼
┌────────────────────┐    ┌──────────────────────┐
│ tpl_template       │    │ tpl_submission       │
│ tpl_template_version│   │  submission_json (原始)│
│ tpl_tag_mapping    │    │  extracted_data (JSON)│
└────────────────────┘    └──────────────────────┘
                                     │
                                     ▼
                          ┌──────────────────────┐
                          │ SpreadsheetData      │
                          │ Extractor            │
                          │                      │
                          │ Tag → 结构化字段映射    │
                          │ → de_* 业务表          │
                          └──────────────────────┘
```

### 核心机制:
1. **模板设计**: 管理员通过SpreadJS Designer设计模板, 为需要抽取的单元格设置Tag或Named Range
2. **标签映射**: tpl_tag_mapping 记录 tagName → fieldName/targetTable 的映射关系
3. **数据填报**: 企业端加载模板, 填写数据, 保存时前端提取Tag值 + 完整JSON一并提交
4. **数据抽取**: SpreadsheetDataExtractor 根据Tag映射将数据写入对应 de_* 业务表
5. **双存储**: tpl_submission 存原始JSON(可重建), de_* 表存结构化数据(可查询/统计)
6. **版本管理**: 历史数据绑定原模板版本, 新填报使用最新发布版本

### 悲观锁机制:
- 编辑前: 调用 acquireLock(enterpriseId, templateId, auditYear)
- 锁定30分钟自动过期(防死锁)
- 保存/退出时: releaseLock()
- 其他用户尝试编辑: 提示"当前文档正在被其他用户编辑中"

## 6. 能源流程图架构 (AntV X6)

```
┌─────────────────────────────────────────┐
│        AntV X6 Graph Canvas             │
│                                         │
│  ┌─────┐    energy     ┌──────────┐     │
│  │外购  │─────────────→│ 加工转换  │     │
│  │能源  │   实物量      │  单元     │     │
│  └─────┘              └──┬───────┘     │
│                          │              │
│                    ┌─────▼─────┐        │
│                    │ 输送分配   │        │
│                    │  单元     │        │
│                    └─────┬────┘        │
│                          │              │
│                    ┌─────▼─────┐        │
│                    │ 终端使用   │        │
│                    │  单元     │        │
│                    └──────────┘        │
└─────────────────────────────────────────┘
```

- 节点类型: 外购能源 / 加工转换单元 / 输送分配单元 / 终端使用单元 / 产品 / 非生产系统
- 连线属性: 源→目的 + 关联能源(bs_energy) + 实物量
- 三种输入模式: 分层图式(4.11.2) / 单元图式(4.11.3) / 二维表式(4.11.4)
- 数据存储: de_energy_flow_diagram(图布局) + de_energy_flow_node(节点) + de_energy_flow_edge(连线)

## 7. 审计报告架构 (OnlyOffice)

```
数据录入完成 → 系统自动生成初始报告(Word) → OnlyOffice在线编辑 → 下载/上传最终报告
     │                  │                        │
     ▼                  ▼                        ▼
de_* 业务数据     ar_report(status=2)      ar_report(status=3/4)
                 ar_report_section         ar_report_version
                 generated_file_path       uploaded_file_path
```

- OnlyOffice Document Server 部署为独立服务
- 通过 onlyoffice_doc_key 关联文档
- 报告章节从 de_* 数据自动填充

## 8. 审核工作流

```
企业提交 ──→ 待审核(0) ──→ 审核中(1) ──→ 已通过(2) ──→ 已完成(4)
                              │
                              └──→ 已退回(3) ──→ 企业整改 ──→ 重新提交
                                                    │
                                            aw_rectification_track
                                            (未启动/进行中/已完成/超期)
```

- 单级审核, 无多级流转
- 超期未整改自动预警(定时任务检查 deadline)
- aw_audit_log 记录每次操作

## 9. 缓存策略 (Ehcache 3)

| 缓存名 | 容量 | TTL | 用途 |
|--------|------|-----|------|
| dictCache | 500 | 1h | 字典数据缓存 |
| energyCache | 200 | 30m | 能源品种缓存 |
| templateCache | 100 | 1h | 模板元数据缓存 |

## 10. API 设计规范

- 基础路径: `/api`
- 认证: JWT Token, Header `Authorization: Bearer {token}`
- 响应格式: `R<T>` { code, message, data }
- 分页: `PageResult<T>` { total, rows }
- 错误码: 200成功 / 400参数错误 / 401未认证 / 403无权限 / 500内部错误

### 核心API列表

| 模块 | 路径 | 说明 |
|------|------|------|
| 认证 | POST /api/auth/login | 登录 |
| 认证 | POST /api/auth/logout | 登出 |
| 认证 | GET /api/auth/info | 当前用户信息 |
| 用户 | /api/system/users | 用户CRUD |
| 字典 | /api/system/dicts | 字典CRUD |
| 企业 | /api/enterprise | 企业CRUD |
| 注册 | /api/registration | 注册申请/审核 |
| 能源设置 | /api/setting/energy | 能源品种CRUD |
| 单元设置 | /api/setting/unit | 用能单元CRUD |
| 产品设置 | /api/setting/product | 产品CRUD |
| 模板 | /api/template | 模板CRUD/发布/数据抽取 |
| 模板锁 | /api/template/lock | 获取/释放/检查编辑锁 |
| 数据录入 | /api/entry/{moduleCode} | 各模块数据CRUD |
| 能流图 | /api/flow-diagram | 流程图CRUD |
| 报告 | /api/report | 报告生成/上传/查询 |
| 审核 | /api/audit/task | 审核任务管理 |
| 碳排放 | /api/carbon/factor | 排放因子管理 |
| 图表 | /api/chart | 图表查询/导出 |

## 11. 安全设计

- 密码: BCrypt 加密存储
- 认证: JWT Token (24h过期)
- 企业首次登录: 强制修改初始密码
- 数据隔离: 所有业务查询携带 enterprise_id 过滤
- 软删除: deleted 字段, 不物理删除
- 操作日志: sys_operation_log 记录关键操作
- 文件上传: 限制50MB, 类型白名单校验

## 12. 目录结构总览

```
/project/workspace/
├── pom.xml                               # Maven Parent
├── sql/schema.sql                        # 数据库DDL
├── ARCHITECTURE.md                       # 本文档
├── audit-common/                         # 通用模块
│   └── src/main/java/.../common/
│       ├── result/     (R, PageResult)
│       ├── exception/  (BusinessException, GlobalExceptionHandler)
│       ├── constant/   (Constants)
│       ├── enums/      (UserType, AuditStatus)
│       └── util/       (SecurityUtils)
├── audit-model/                          # 数据模型
│   └── src/main/java/.../model/
│       ├── entity/     (55张表实体类)
│       └── dto/        (LoginDTO, PageDTO)
├── audit-dao/                            # 数据访问
│   └── src/main/java/.../dao/mapper/
├── audit-service/                        # 业务逻辑
│   └── src/main/java/.../service/
│       ├── system/     (Auth, User)
│       ├── enterprise/ (Enterprise)
│       ├── setting/    (Energy)
│       └── template/   (Template, DataExtractor, EditLock)
├── audit-web/                            # Web层
│   └── src/main/
│       ├── java/.../web/
│       │   ├── config/       (WebMvc, Cache, MyBatis, Security)
│       │   ├── interceptor/  (JwtAuth)
│       │   └── controller/   (8个Controller)
│       └── resources/
│           ├── application.yml
│           └── ehcache.xml
└── audit-ui/                             # 前端
    ├── package.json
    ├── vite.config.ts
    └── src/
        ├── api/          (4个API模块)
        ├── components/   (SpreadSheet, FlowEditor, DocEditor)
        ├── layouts/      (3个布局)
        ├── router/       (3端路由, 40+页面)
        ├── stores/       (user, app)
        ├── utils/        (request)
        └── views/        (46个页面组件)
```
