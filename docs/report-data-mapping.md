# 能源审计报告 — 完整数据映射文档

> 本文档逐章逐节列出报告中每一项数据的来源表和字段，确保报告生成时无遗漏。
> 标注说明：
> - **[表格]** = 报告中的编号表格（表1~表22）
> - **[文本]** = 需要用户手动录入的文字段落，存储在 `de_report_text.content`（按 `section_code` 区分）
> - **[图表]** = 需要 ECharts 生成的图表，渲染为图片后插入 Word
> - **[计算]** = 需要后端计算得出的值
> - **[元数据]** = 来自企业注册信息 `ent_enterprise` / `ent_enterprise_setting` 表

---

## 封面页

| 报告数据项 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 报告编号（上海能审2021-×××） | `ar_report` | `report_name` 或生成规则 | 格式：城市名+能审+年份+序号 |
| 企业名称 | `ent_enterprise` | `enterprise_name` | |
| 编制单位 | `ent_enterprise_setting` | `compiler_name` | |
| 日期 | `ar_report` | `generate_time` | 生成时间 |

## 参加能源审计人员表

| 报告数据项 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 姓名 | `de_submission_table` | tag_name=`audit_team` | 审计小组人员名单，通用表存储 |
| 审计小组职务 | `de_submission_table` | tag_name=`audit_team`, column `role` | |
| 职务/职称 | `de_submission_table` | tag_name=`audit_team`, column `title` | |
| 工作单位 | `de_submission_table` | tag_name=`audit_team`, column `work_unit` | |
| 报告编写人 | `de_submission_field` | tag_name=`report_writer` | |
| 报告校对人 | `de_submission_field` | tag_name=`report_reviewer` | |
| 报告审核人 | `de_submission_field` | tag_name=`report_auditor` | |
| 报告签发人 | `de_submission_field` | tag_name=`report_issuer` | |

## 能源审计基本信息表（规定图表）

| 报告数据项 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 企业名称 | `ent_enterprise` | `enterprise_name` | |
| 统一社会信用代码 | `ent_enterprise` | `credit_code` | |
| 法人代表 | `ent_enterprise_setting` | `legal_representative` | |
| 地址 | `ent_enterprise_setting` | `enterprise_address` | |
| 邮编 | `ent_enterprise_setting` | `postal_code` | |
| 传真 | `ent_enterprise_setting` | `fax` | |
| 联系人 | `ent_enterprise` | `contact_person` | |
| 联系电话 | `ent_enterprise` | `contact_phone` | |
| 行业类别 | `ent_enterprise_setting` | `industry_category` | |
| 行业代码 | `ent_enterprise_setting` | `industry_code` | |
| 编制单位 | `ent_enterprise_setting` | `compiler_name` | |

## 审计摘要（编写提纲）

### 第1项：企业能源审计的主要任务和内容
- **[文本]** `de_report_text` → `section_code='summary_task'`

### 第2项：企业能源消费情况

| 报告数据项 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 综合能耗（吨标准煤） | `de_tech_indicator` | `total_energy_equal` | 当年(indicator_year=审计年) |
| 占企业综合能耗比重(%) | **[计算]** | 各产品能耗/总能耗 | |
| 主要产品A名称 | `de_product_output` | `product_name` | 第1行 |
| 主要产品B名称 | `de_product_output` | `product_name` | 第2行 |
| 企业工业总产值 | `de_tech_indicator` | `gross_output` | 当年 |
| 万元产值能耗 | `de_tech_indicator` | `unit_output_energy_equal` | 当年 |
| 企业总能耗 | `de_tech_indicator` | `total_energy_equal` | 当年 |
| 能源消耗情况分析（文字） | `de_report_text` | `section_code='summary_energy'` | |

### 第3项：企业碳排放情况

| 报告数据项 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 直接排放-化石燃料燃烧 | `de_carbon_emission` | `co2_emission` WHERE `emission_category='direct_fossil'` | |
| 直接排放-过程排放 | `de_carbon_emission` | `co2_emission` WHERE `emission_category='direct_process'` | |
| 直接排放-废弃物焚烧 | `de_carbon_emission` | `co2_emission` WHERE `emission_category='direct_waste'` | |
| 直接排放-物料平衡法 | `de_carbon_emission` | `co2_emission` WHERE `emission_category='direct_material'` | |
| 间接排放-外购电力 | `de_carbon_emission` | `co2_emission` WHERE `emission_category='indirect_power'` | |
| 间接排放-外购热力 | `de_carbon_emission` | `co2_emission` WHERE `emission_category='indirect_heat'` | |
| 总排放量 | **[计算]** | SUM(`co2_emission`) | |

### 第4项：能源利用效率

| 报告数据项 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 产品名称 | `de_product_unit_consumption` | `indicator_name` | |
| 计量单位 | `de_product_unit_consumption` | `indicator_unit` | |
| 产品能源单耗 | `de_product_unit_consumption` | `current_indicator` | |
| 单耗水平情况（文字） | `de_report_text` | `section_code='summary_efficiency'` | |

### 第5项：节能降碳目标完成情况

| 报告数据项 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 节能降碳目标名称 | `de_company_overview` | `five_year_target_name` | |
| 计量单位 | 固定值 | "吨标煤" | |
| 目标下达部门 | `de_company_overview` | `five_year_target_dept` | |
| 目标值 | `de_company_overview` | `five_year_target_value` | |
| 实际完成值 | `de_tech_indicator` | `coal_actual` | 当年 |
| 目标完成分析（文字） | `de_report_text` | `section_code='summary_target'` | |
| 节能效益分析（文字） | `de_report_text` | `section_code='summary_benefit'` | |

### 第6项：节能降碳措施
- **[文本]** `de_report_text` → `section_code='summary_measures'`

### 第7项：对企业节能降碳管理工作的总体评估
- **[文本]** `de_report_text` → `section_code='summary_evaluation'`

### 第8项：审计结论和建议
- **[文本]** `de_report_text` → `section_code='summary_conclusion'`

---

## 第一章 审计事项说明

### 1.1 任务来源
- **[文本]** `de_report_text` → `section_code='1.1'`，`section_name='任务来源'`

### 1.2 审计目的
- **[文本]** `de_report_text` → `section_code='1.2'`，`section_name='审计目的'`

### 1.3 审计依据
- **[文本]** `de_report_text` → `section_code='1.3'`，`section_name='审计依据'`
- 报告模板中预置了示例法规列表，用户可在此基础上增减

### 1.4 审计期、范围和内容
- **[文本]** `de_report_text` → `section_code='1.4'`，`section_name='审计期范围和内容'`
- 同时引用：`de_report_text` → `section_code='4.4_intro'`（温室气体排放审核说明）

### 1.5 审计情况说明
- **[文本]** `de_report_text` → `section_code='1.5'`，`section_name='审计情况说明'`

#### 表1. 已实施的节能技改项目表

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 序号 | `de_tech_reform_history` | `seq_no` | |
| 项目名称 | `de_tech_reform_history` | `project_name` | |
| 主要内容 | `de_tech_reform_history` | `main_content` | CLOB |
| 投资（万元） | `de_tech_reform_history` | `investment` | |
| 年节能量（吨标煤） | `de_tech_reform_history` | `designed_saving` | 设计节能量 |
| 投资回收期（年） | `de_tech_reform_history` | `payback_period` | |
| 完成时间 | `de_tech_reform_history` | `completion_date` | |
| 实际节能量(吨标煤) | `de_tech_reform_history` | `actual_saving` | |
| 是否合同能源管理模式 | `de_tech_reform_history` | `is_contract_energy` | "是"/"否" |
| 备注 | `de_tech_reform_history` | `remark` | |

---

## 第二章 企业基本情况

### 2.1 企业简况
- **[文本]** `de_report_text` → `section_code='2.1'`，`section_name='企业简介'`

#### 表2. 企业基本情况及主要经济技术指标

**上半部分 — 企业基本情况：**

| 报告代码 | 报告项目名称 | 数据来源表 | 字段 |
|---|---|---|---|
| 1 | 单位名称 | `ent_enterprise` | `enterprise_name` |
| 2 | 法人代表 | `ent_enterprise_setting` | `legal_representative` |
| 3 | 节能主管领导姓名/职务 | `de_company_overview` | `energy_leader_name` + `energy_leader_position` |
| 4 | 节能主管部门名称 | `de_company_overview` | `energy_dept_name` |
| 5 | 节能管理部门负责人姓名 | `de_company_overview` | `energy_dept_leader` |
| 6 | 专职管理人数 | `de_company_overview` | `fulltime_staff_count` |
| 7 | 兼职管理人数 | `de_company_overview` | `parttime_staff_count` |
| 8 | "十四五"期间节能目标名称 | `de_company_overview` | `five_year_target_name` |
| 9 | "十四五"期间节能目标值 | `de_company_overview` | `five_year_target_value` |
| 10 | "十四五"期间节能目标下达部门 | `de_company_overview` | `five_year_target_dept` |

**下半部分 — 主要经济技术指标（双年对比）：**
> 数据来源：`de_tech_indicator` 表，通过 `indicator_year` 区分"今年"和"去年"

| 报告代码 | 报告项目名称 | 数据来源字段 | 说明 |
|---|---|---|---|
| 1 | 工业总产值(现价) | `gross_output` | 两年各一行 |
| 2 | 销售收入 | `sales_revenue` | |
| 3 | 上缴利税 | `tax_paid` | |
| 4 | 综合能耗(当量值) | `total_energy_equiv` | |
| 5 | 综合能耗(等价值) | `total_energy_equal` | |
| 6 | 综合能耗（剔除原料用能） | `total_energy_excl_material` | |
| 7 | 单位产值综合能耗(当量值) | `unit_output_energy_equiv` | |
| 8 | 单位产值综合能耗(等价值) | `unit_output_energy_equal` | |
| 9 | 生产总成本 | `production_cost` | |
| 10 | 能源消费成本 | `energy_total_cost` | |
| 11 | 能源消费占成本比例 | `energy_cost_ratio` | |
| 12 | 完成节能项目数目 | `saving_project_count` | |
| 13 | 完成节能项目投资总量 | `saving_invest_total` | |
| 14 | 完成节能项目节能能力 | `saving_capacity` | |
| 15 | 完成节能项目经济效益 | `saving_benefit` | |
| 16 | 年度节能目标（煤炭消费总量） | `coal_target` | |
| 17 | 年度节能目标完成值 | `coal_actual` | |
| — | 增减% | **[计算]** | (今年-去年)/去年 |

**表2下方自动生成的描述段落：**
> "太金空调有限公司 2020年工业总产值500000.0万元..."
> 所有数值来自上表对应字段，通过模板拼接自动生成。

### 2.2 主要产品生产工艺概况

| 报告数据项 | 数据来源 | 说明 |
|---|---|---|
| 主要工艺、装置、主要设备名称及生产能力 | `de_report_text` → `section_code='2.2_process'` | **[文本]** |
| 工艺介绍 | `de_report_text` → `section_code='2.2_intro'` | **[文本]** |
| 主要工艺能源消耗情况 | `de_report_text` → `section_code='2.2_consumption'` | **[文本]** |
| 节能减碳潜力和改进建议 | `de_report_text` → `section_code='7.1_potential'` | **[文本]**（交叉引用第七章） |

#### 表3. 主要用能设备汇总表

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 序号 | 行号自动生成 | | |
| 设备名称 | `de_equipment_detail` | `equipment_name` | |
| 分类 | `de_equipment_detail` | `equipment_type` | 分类标识 |
| 型号 | `de_equipment_detail` | `model` | |
| 容量 | `de_equipment_detail` | `capacity` | |
| 数量 | `de_equipment_detail` | `quantity` | |
| 设备概况 | `de_equipment_detail` | `equipment_overview` | |
| 淘汰更新情况 | `de_equipment_detail` | `obsolete_status` | |
| 安装使用场所 | `de_equipment_detail` | `install_location` | |
| 年运行时间(小时) | `de_equipment_detail` | `annual_runtime_hours` | |

### 2.3 主要供能或耗能工质系统情况

每个子系统均为**[文本]**，存储在 `de_report_text`：

| 小节 | section_code | 需要的文本内容 |
|---|---|---|
| 2.3.1 电力系统 | `2.3.1` | 电力系统情况 + 电力系统说明 |
| 2.3.2 供热系统 | `2.3.2` | 供热系统情况 + 说明（或"无配置"） |
| 2.3.3 制冷系统 | `2.3.3` | 制冷系统情况 + 说明 |
| 2.3.4 压缩空气系统 | `2.3.4` | 压缩空气系统情况 + 说明 |
| 2.3.5 循环水系统 | `2.3.5` | 循环水系统情况 + 说明 |
| 2.3.6 其他系统 | `2.3.6` | 其他系统说明 |

### 2.4 能源流程图
- **[流程图]** 画布式能源流程图（非 Sankey 图）
- 数据来源：`de_energy_flow` 表
- 字段：`source_unit`(源单元) → `target_unit`(目标单元)，`energy_product`(能源种类)，`physical_quantity`(实物量)，`standard_quantity`(标煤量)，`flow_stage`(流转阶段)
- **节点类型（6类）**：
  1. **外购能源** — 企业外部购入的各类能源（电力、天然气、蒸汽等）
  2. **加工转换单元** — 能源加工转换设备/系统（锅炉、发电机等）
  3. **输送分配单元** — 能源输送分配网络（配电系统、管道等）
  4. **终端使用单元** — 各车间/工序的终端用能设备
  5. **产品** — 最终产出的产品（含能源回收）
  6. **非生产系统** — 办公、照明、空调等辅助用能
- 渲染方式：AntV X6 画布流程图（`FlowEditor.vue` 组件已有骨架），节点按类型分色，连线标注能源种类和流量值
- **与报告的关系**：该能源流程图模块独立开发完成后，可直接将画布导出为图片插入 Word 报告的 2.4 节
- 报告插入方式：FlowEditor 画布截图导出 PNG → 插入 Word

---

## 第三章 企业能源管理运行状况分析

### 3.1 企业能源管理方针和目标
- **[文本]** `de_report_text` → `section_code='3.1'`

### 3.2 企业能源管理机构和职权
- **[文本]** `de_report_text` → `section_code='3.2'`

### 3.3 企业能源文件管理
- **[文本]** `de_report_text` → `section_code='3.3'`

#### 表4. 能源管理制度表

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 序号 | `de_management_policy` | `seq_no` | |
| 能源管理制度名称 | `de_management_policy` | `policy_name` | |
| 主要内容 | `de_management_policy` | `main_content` | CLOB |
| 主管部门 | `de_management_policy` | `supervise_dept` | |
| 颁布日期 | `de_management_policy` | `publish_date` | |
| 有效期 | `de_management_policy` | `valid_period` | |

### 3.4 企业能源计量管理

#### 表5. 计量器具配备情况

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 管理编号 | `de_meter_instrument` | `management_no` | |
| 计量表名称 | `de_meter_instrument` | `meter_name` | |
| 安装地点或计量区域 | `de_meter_instrument` | `install_location` | |
| 型号规格 | `de_meter_instrument` | `model_spec` | |
| 倍率 | `de_meter_instrument` | `multiplier` | |
| 级别 | `de_meter_instrument` | `accuracy_class` | |
| 能源属性 | `de_meter_instrument` | `energy_type` | |
| 所属部门 | `de_meter_instrument` | `department` | |
| 准确度等级 | `de_meter_instrument` | `accuracy_grade` | |
| 测量范围 | `de_meter_instrument` | `measurement_range` | |
| 生产厂家 | `de_meter_instrument` | `manufacturer` | |
| 出厂编号 | `de_meter_instrument` | `serial_no` | |
| 状态 | `de_meter_instrument` | `status` | 合格/不合格/停用 |

#### 表6. 能源计量器具配备率表

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 能源种类 | `de_meter_config_rate` | `energy_type` | |
| 配备级别（进出用能单位/次级/主要设备） | `de_meter_config_rate` | `config_level` | 区分3个级别 |
| 配备率标准% | `de_meter_config_rate` | `standard_rate` | |
| 需要配置数 | `de_meter_config_rate` | `required_count` | |
| 实际配置数 | `de_meter_config_rate` | `actual_count` | |
| 实际配备率 | `de_meter_config_rate` | `actual_rate` 或 **[计算]** actual_count/required_count | |

**计量管理文字说明：**
- 能源计量系统说明 → `de_report_text` → `section_code='3.4_metering'`
- 能源计量管理建议 → `de_report_text` → `section_code='3.4_suggestion'`

### 3.5 企业能源统计管理
- 目前统计工作介绍 → `de_report_text` → `section_code='3.5_current'`
- 对于统计工作的建议 → `de_report_text` → `section_code='3.5_suggestion'`

---

## 第四章 企业能源统计和温室气体排放数据审核

### 4.1 对能源数据和温室气体排放源数据进行审核
- **[文本]** `de_report_text` → `section_code='4.1'`

#### 表7. 能源数据和温室气体排放源数据审核（辅助表格）

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 能源种类 | `de_energy_consumption` | `energy_name` | |
| 计量单位 | `de_energy_consumption` | `measurement_unit` | |
| 期初购存 | `de_energy_consumption` | `opening_stock` | |
| 期内购入 | `de_energy_consumption` | `purchase_total` | |
| 期内消耗 | `de_energy_consumption` | `consumption_total` 或 `industrial_consumption` | |
| 期末库存 | `de_energy_consumption` | `closing_stock` | |

### 4.2 对与能耗相关数据审核

#### 4.2.1 对工业总产值及产品产量数据的审核
- **[文本]** `de_report_text` → `section_code='4.2'`

#### 表8. 工业总产值和产量审核表
> 数据来源：`de_tech_indicator` 表，按 `indicator_year` 筛选近3年

| 报告表头 | 数据来源字段 | 说明 |
|---|---|---|
| 工业总产量 | `gross_output` | 3年数据 |
| 工业增加值 | 需要新增字段或从 `de_submission_field` 取 | 目前表中无此字段 |
| 销售收入 | `sales_revenue` | 3年数据 |
| 利税 | `tax_paid` | 3年数据 |
| 产量 | `de_product_output` → `annual_output` | 主要产品产量 |
| 综合能耗(等价值) | `total_energy_equal` | 3年数据 |

#### 4.2.2 对企业购入能源费用、单价和用能水平的审核

#### 表9. 购入能源费用表

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 能源名称 | `de_energy_consumption` | `energy_name` | |
| 计量单位 | `de_energy_consumption` | `measurement_unit` | |
| 期内购入 | `de_energy_consumption` | `purchase_total` | |
| 购入费用（万元） | **[计算]** | `purchase_total * unit_price` | |
| 单价（万元） | `de_energy_consumption` | `unit_price` | |
| 期内消耗 | `de_energy_consumption` | `consumption_total` | |
| 消费费用 | **[计算]** | `consumption_total * unit_price` | |

#### 表10. 产值能耗情况表（等价值）
> 数据来源：`de_tech_indicator`（3年）+ `de_product_output`

| 报告表头 | 数据来源字段 | 说明 |
|---|---|---|
| 年份 | `indicator_year` | 近3年 |
| 工业总产值（万元） | `gross_output` | |
| 产量单位 | `de_product_output` → `output_unit` | |
| 综合能耗（tce） | `total_energy_equal` | |
| 单位产值能耗（tce/万元） | `unit_output_energy_equal` | |
| 产品单耗tce/t | `de_product_unit_consumption` → `current_indicator` | |

**表10下方自动生成的描述段落：**
> "太金空调有限公司年2020的综合能耗为14000.0tce，比2019年的13000.0tce降低了..."
> 所有数值从 `de_tech_indicator` 当年/去年数据计算得出。

**[图表]** 产值能耗趋势图
- 数据来源：`de_tech_indicator`（多年 `indicator_year` 数据）
- X轴：年份，Y轴：单位产值能耗
- 类型：折线图

### 4.3 对采用的能源折标系数和相关参数的审核
- **[文本]** `de_report_text` → `section_code='4.3'`

#### 表11. 能源折标系数表

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 能源名称 | `de_energy_consumption` | `energy_name` | |
| 计量单位 | `de_energy_consumption` | `measurement_unit` | |
| 折标系数（当量值） | `de_energy_consumption` | `equiv_factor` | |
| 折标系数（等价值） | `de_energy_consumption` | `equal_factor` | |

### 4.4 对温室气体排放量、碳排放强度数据进行审核

#### 表12. 温室气体排放量表

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 温室气体种类 | 固定文本 | CO2/CH4/N2O/HFCs/PFCs/SF6/NF3 | |
| 排放类型 | `de_carbon_emission` | `emission_category` | |
| 排放量（tCO2） | `de_carbon_emission` | `co2_emission` | |
| 直接排放-化石燃料燃烧 | `de_carbon_emission` | WHERE `emission_category='direct_fossil'` | |
| 直接排放-过程排放 | `de_carbon_emission` | WHERE `emission_category='direct_process'` | |
| 间接排放-外购电力 | `de_carbon_emission` | WHERE `emission_category='indirect_power'` | |
| 间接排放-外购热力 | `de_carbon_emission` | WHERE `emission_category='indirect_heat'` | |
| 总排放量 | **[计算]** | SUM(co2_emission) | |

**[图表]** 温室气体排放构成饼图
- 数据来源：`de_carbon_emission`，按 `emission_category` 分组
- 类型：饼图

#### 表13. 能源总排放量表

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 能源类型（燃料类型） | `de_carbon_emission` | `source_name` | 详细行 |
| 主要用能设备/生产部门 | `de_carbon_emission` | `remark` 或通过关联 `de_equipment_detail` | |
| 计量单位 | `de_carbon_emission` | `measurement_unit` 或从 `de_energy_consumption` 关联 | |
| 活动数据 | `de_carbon_emission` | `activity_data` | |
| 年度排放量（tCO2） | `de_carbon_emission` | `co2_emission` | |
| 低位热值 | `de_carbon_emission` | `low_heat_value` | |
| 含碳量 | `de_carbon_emission` | `carbon_content` | |
| 氧化率 | `de_carbon_emission` | `oxidation_rate` | |
| 排放因子 | `de_carbon_emission` | `emission_factor` | |

---

## 第五章 企业用能情况分析

### 5.1 能源消费结构

#### 表14. 能源消费汇总表

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 能源名称 | `de_energy_consumption` | `energy_name` | |
| 计量单位 | `de_energy_consumption` | `measurement_unit` | |
| 购入量 | `de_energy_consumption` | `purchase_total` | |
| 消费量（实物量） | `de_energy_consumption` | `industrial_consumption` | |
| 折标量（吨标煤） | `de_energy_consumption` | `standard_coal` | |
| 占综合能耗比重 | **[计算]** | standard_coal / SUM(standard_coal) | |

**[图表]** 能源消费结构饼图
- 数据来源：`de_energy_consumption`，按 `energy_name` 分组
- 数值：`standard_coal`（吨标煤）
- 类型：饼图

### 5.2 企业能源加工转换

#### 表15. 能源加工转换汇总表

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 能源名称 | `de_energy_conversion` | `energy_name` | |
| 计量单位 | `de_energy_conversion` | `measurement_unit` | |
| 工业消费 | `de_energy_conversion` | `industrial_consumption` | |
| 加工转换投入合计 | `de_energy_conversion` | `conversion_input_total` | |
| 发电 | `de_energy_conversion` | `conv_power_gen` | |
| 供热 | `de_energy_conversion` | `conv_heating` | |
| 洗煤 | `de_energy_conversion` | `conv_coal_washing` | |
| 炼焦 | `de_energy_conversion` | `conv_coking` | |
| 炼油 | `de_energy_conversion` | `conv_refining` | |
| 制气 | `de_energy_conversion` | `conv_gas_making` | |
| 液化天然气 | `de_energy_conversion` | `conv_lng` | |
| 煤制品 | `de_energy_conversion` | `conv_coal_product` | |
| 加工转换产出 | `de_energy_conversion` | `conversion_output` | |
| 产出折标量 | `de_energy_conversion` | `conversion_output_std` | |
| 回收利用 | `de_energy_conversion` | `recovery_utilization` | |

### 5.3 各单元能源消费

#### 表16. 能源消费平衡表

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 行标签（用能单元名称） | `de_energy_balance` | `row_label` | |
| 行类别（车间/工序等） | `de_energy_balance` | `row_category` | |
| 能源名称 | `de_energy_balance` | `energy_name` | |
| 能源值 | `de_energy_balance` | `energy_value` | |
| 计量单位 | `de_energy_balance` | `measurement_unit` | |
| 排序 | `de_energy_balance` | `row_seq` | |

**[图表]** 各单元能耗分布柱状图
- 数据来源：`de_energy_balance`，按 `row_label` 分组，SUM(`energy_value`)
- 类型：柱状图

### 5.4 产品能源成本分析

#### 表17. 产品能源成本表

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 序号 | `de_product_energy_cost` | `seq_no` | |
| 产品名称 | `de_product_energy_cost` | `product_name` | |
| 能源成本 | `de_product_energy_cost` | `energy_cost` | |
| 生产成本 | `de_product_energy_cost` | `production_cost` | |
| 能源成本占比 | `de_product_energy_cost` | `cost_ratio` | |
| 能源占总比 | `de_product_energy_cost` | `energy_total_ratio` | |

### 5.5 综合能耗计算

#### 表18. 节能量计算表

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 审计年综合能耗（等价值） | `de_saving_calculation` | `energy_equal_current` | |
| 审计年综合能耗（当量值） | `de_saving_calculation` | `energy_equiv_current` | |
| 审计年工业总产值 | `de_saving_calculation` | `gross_output_current` | |
| 审计年产品产量 | `de_saving_calculation` | `product_output_current` | |
| 基准年综合能耗（等价值） | `de_saving_calculation` | `energy_equal_base` | |
| 基准年综合能耗（当量值） | `de_saving_calculation` | `energy_equiv_base` | |
| 基准年工业总产值 | `de_saving_calculation` | `gross_output_base` | |
| 基准年产品产量 | `de_saving_calculation` | `product_output_base` | |
| 节能量 | **[计算]** | base - current（按公式计算） | |

**[图表]** 综合能耗趋势折线图
- 数据来源：`de_tech_indicator`（多年 `indicator_year`）
- X轴：年份，Y轴：`total_energy_equal`（综合能耗等价值）
- 类型：折线图

---

## 第六章 主要用能设备及系统节能分析

### 6.1 主要用能设备分析
- **[文本]** `de_report_text` → `section_code='6.1'`

#### 表19. 主要用能设备运行效率

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 设备名称 | `de_equipment_detail` | `equipment_name` | WHERE equipment_type 指定类型 |
| 型号 | `de_equipment_detail` | `model` | |
| 容量 | `de_equipment_detail` | `capacity` | |
| 数量 | `de_equipment_detail` | `quantity` | |
| 年运行时间(小时) | `de_equipment_detail` | `annual_runtime_hours` | |
| 年耗能量 | `de_equipment_detail` | `annual_energy` | |
| 能源单位 | `de_equipment_detail` | `energy_unit` | |
| 能效水平 | `de_equipment_detail` | `energy_efficiency` | |
| 详细参数(JSON) | `de_equipment_detail` | `detail_json` | 按设备类型展开 |

### 6.2 淘汰设备情况

#### 表20. 淘汰设备清单

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 序号 | `de_obsolete_equipment` | `seq_no` | |
| 设备名称 | `de_obsolete_equipment` | `equipment_name` | |
| 型号规格 | `de_obsolete_equipment` | `model_spec` | |
| 数量 | `de_obsolete_equipment` | `quantity` | |
| 启用日期 | `de_obsolete_equipment` | `start_use_date` | |
| 计划淘汰日期 | `de_obsolete_equipment` | `planned_retire_date` | |
| 备注 | `de_obsolete_equipment` | `remark` | |

### 6.3 产品单位能耗分析

#### 表21. 主要产品单位能耗情况表

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 产品能耗指标名称 | `de_product_unit_consumption` | `indicator_name` | |
| 计量单位 | `de_product_unit_consumption` | `indicator_unit` | |
| 分子单位 | `de_product_unit_consumption` | `numerator_unit` | |
| 分母单位 | `de_product_unit_consumption` | `denominator_unit` | |
| 转换系数 | `de_product_unit_consumption` | `conversion_factor` | |
| 当年指标值 | `de_product_unit_consumption` | `current_indicator` | |
| 当年分子值（能耗） | `de_product_unit_consumption` | `current_numerator` | |
| 当年分母值（产量） | `de_product_unit_consumption` | `current_denominator` | |
| 上年指标值 | `de_product_unit_consumption` | `previous_indicator` | |
| 上年分子值 | `de_product_unit_consumption` | `previous_numerator` | |
| 上年分母值 | `de_product_unit_consumption` | `previous_denominator` | |

**[图表]** 单位产品能耗对比柱状图
- 数据来源：`de_product_unit_consumption`
- X轴：指标名称，Y轴分组：当年/上年
- 类型：分组柱状图

---

## 第七章 审计结论与节能降碳建议

### 7.1 节能潜力分析

#### 表22a. 节能潜力分析表

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 序号 | `de_saving_potential` | `seq_no` | |
| 分类 | `de_saving_potential` | `category` | |
| 项目名称 | `de_saving_potential` | `project_name` | |
| 主要内容 | `de_saving_potential` | `main_content` | |
| 节能潜力（吨标煤） | `de_saving_potential` | `saving_potential` | |
| 计算说明 | `de_saving_potential` | `calc_description` | |
| 备注 | `de_saving_potential` | `remark` | |

### 7.2 能源管理改进建议

#### 表22b. 能源管理改进建议表

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 序号 | `de_management_suggestion` | `seq_no` | |
| 项目名称 | `de_management_suggestion` | `project_name` | |
| 主要内容 | `de_management_suggestion` | `main_content` | |
| 投资（万元） | `de_management_suggestion` | `investment` | |
| 年节能量（吨标煤） | `de_management_suggestion` | `annual_saving` | |
| 备注 | `de_management_suggestion` | `remark` | |

### 7.3 节能技改建议

#### 表22c. 节能技改建议表

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 序号 | `de_tech_reform_suggestion` | `seq_no` | |
| 项目名称 | `de_tech_reform_suggestion` | `project_name` | |
| 主要内容 | `de_tech_reform_suggestion` | `main_content` | |
| 投资（万元） | `de_tech_reform_suggestion` | `investment` | |
| 年节能量（吨标煤） | `de_tech_reform_suggestion` | `annual_saving` | |
| 投资回收期（年） | `de_tech_reform_suggestion` | `payback_period` | |
| 备注 | `de_tech_reform_suggestion` | `remark` | |

### 7.4 整改措施

#### 表22d. 整改措施表

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 序号 | `de_rectification` | `seq_no` | |
| 项目名称 | `de_rectification` | `project_name` | |
| 整改措施 | `de_rectification` | `measures` | |
| 目标日期 | `de_rectification` | `target_date` | |
| 责任人 | `de_rectification` | `responsible_person` | |
| 估算投资 | `de_rectification` | `estimated_cost` | |
| 年节能量 | `de_rectification` | `annual_saving` | |
| 年经济效益 | `de_rectification` | `annual_benefit` | |

### 7.5 "十四五"节能目标

#### 表（附表）. "十四五"期间能源消费与节能目标

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 节类型 | `de_five_year_target` | `section_type` | 区分不同节（产值能耗/产品单耗/能源控制目标等） |
| 年份标签 | `de_five_year_target` | `year_label` | |
| 工业总产值 | `de_five_year_target` | `gross_output` | |
| 综合能耗（当量） | `de_five_year_target` | `energy_equiv` | |
| 综合能耗（等价） | `de_five_year_target` | `energy_equal` | |
| 单位产值能耗（当量） | `de_five_year_target` | `unit_energy_equiv` | |
| 单位产值能耗（等价） | `de_five_year_target` | `unit_energy_equal` | |
| 下降率 | `de_five_year_target` | `decline_rate` | |
| 产品名称 | `de_five_year_target` | `product_name` | |
| 指标名称 | `de_five_year_target` | `indicator_name` | |
| 指标目标值 | `de_five_year_target` | `indicator_value` | |
| 实际值 | `de_five_year_target` | `actual_value` | |
| 能源控制总量 | `de_five_year_target` | `energy_control_total` | |
| 产品单耗 | `de_five_year_target` | `product_unit_consumption` | |
| 节约量 | `de_five_year_target` | `saving_amount` | |

### 7.6 节能措施项目汇总

#### 表（附表）. 拟实施的节能措施项目表

| 报告表头 | 数据来源表 | 字段 | 说明 |
|---|---|---|---|
| 项目类型 | `de_saving_project` | `project_type` | |
| 项目名称 | `de_saving_project` | `project_name` | |
| 实施状态 | `de_saving_project` | `impl_status` | |
| 实施日期 | `de_saving_project` | `impl_date` | |
| 投资（万元） | `de_saving_project` | `investment` | |
| 节能量（吨标煤） | `de_saving_project` | `saving_amount` | |
| 碳减排量 | `de_saving_project` | `carbon_reduction` | |
| 是否合同能源管理 | `de_saving_project` | `is_contract_energy` | |
| 审批部门 | `de_saving_project` | `approval_dept` | |
| 主要内容 | `de_saving_project` | `main_content` | |
| 备注 | `de_saving_project` | `remark` | |

### 审计结论总结
- **[文本]** `de_report_text` → `section_code='7.conclusion'`

---

## 图表汇总（Wave 7 需实现）

| 图表编号 | 图表名称 | 图表类型 | 数据来源表 | 关键字段 | 位置 |
|---|---|---|---|---|---|
| C1 | 能源消费结构饼图 | 饼图 (pie) | `de_energy_consumption` | `energy_name`, `standard_coal` | 5.1节 |
| C2 | 综合能耗趋势折线图 | 折线图 (line) | `de_tech_indicator` | `indicator_year`, `total_energy_equal` | 5.5节 |
| C3 | 各单元能耗分布柱状图 | 柱状图 (bar) | `de_energy_balance` | `row_label`, SUM(`energy_value`) | 5.3节 |
| C4 | 温室气体排放构成饼图 | 饼图 (pie) | `de_carbon_emission` | `emission_category`, SUM(`co2_emission`) | 4.4节 |
| C5 | 单位产品能耗对比柱状图 | 分组柱状图 (bar) | `de_product_unit_consumption` | `indicator_name`, `current_indicator`, `previous_indicator` | 6.3节 |
| C6 | 能源流程图（画布式） | AntV X6 流程图 | `de_energy_flow` | `source_unit`, `target_unit`, `flow_stage`, `energy_product`, `physical_quantity`, `standard_quantity` | 2.4节 |
| C7 | 产值能耗趋势图 | 折线图 (line) | `de_tech_indicator` | `indicator_year`, `unit_output_energy_equal` | 4.2节 |

---

## de_report_text section_code 完整列表

> 所有需要用户手动录入的文字段落，统一存储在 `de_report_text` 表

| section_code | section_name | 报告位置 |
|---|---|---|
| `summary_task` | 企业能源审计的主要任务和内容 | 审计摘要.1 |
| `summary_energy` | 能源消耗情况分析 | 审计摘要.2 |
| `summary_efficiency` | 能源利用效率水平分析 | 审计摘要.4 |
| `summary_target` | 目标完成原因分析 | 审计摘要.5 |
| `summary_benefit` | 节能效益分析 | 审计摘要.5 |
| `summary_measures` | 节能降碳措施 | 审计摘要.6 |
| `summary_evaluation` | 总体评估 | 审计摘要.7 |
| `summary_conclusion` | 审计结论和建议 | 审计摘要.8 |
| `1.1` | 任务来源 | 1.1 |
| `1.2` | 审计目的 | 1.2 |
| `1.3` | 审计依据 | 1.3 |
| `1.4` | 审计期范围和内容 | 1.4 |
| `1.5` | 审计情况说明 | 1.5 |
| `2.1` | 企业简介 | 2.1 |
| `2.2_process` | 主要工艺装置名称及生产能力 | 2.2 |
| `2.2_intro` | 工艺介绍 | 2.2 |
| `2.2_consumption` | 主要工艺能源消耗情况 | 2.2 |
| `2.3.1` | 电力系统 | 2.3.1 |
| `2.3.2` | 供热系统 | 2.3.2 |
| `2.3.3` | 制冷系统 | 2.3.3 |
| `2.3.4` | 压缩空气系统 | 2.3.4 |
| `2.3.5` | 循环水系统 | 2.3.5 |
| `2.3.6` | 其他系统 | 2.3.6 |
| `3.1` | 能源管理方针和目标 | 3.1 |
| `3.2` | 能源管理机构和职权 | 3.2 |
| `3.3` | 能源文件管理 | 3.3 |
| `3.4_metering` | 能源计量系统说明 | 3.4 |
| `3.4_suggestion` | 能源计量管理建议 | 3.4 |
| `3.5_current` | 目前统计工作介绍 | 3.5 |
| `3.5_suggestion` | 统计工作建议 | 3.5 |
| `4.1` | 能源和排放数据审核 | 4.1 |
| `4.2` | 产值和产量数据审核 | 4.2 |
| `4.3` | 能源折标系数审核 | 4.3 |
| `6.1` | 主要用能设备分析 | 6.1 |
| `7.1_potential` | 节能减碳潜力和改进建议 | 7.1（引用于2.2） |
| `7.conclusion` | 审计结论总结 | 7.6 |

共计 **34个** 文本录入段落。

---

## 数据表引用汇总

| 数据表 | 引用的报告位置 |
|---|---|
| `ent_enterprise` | 封面, 基本信息表, 表2 |
| `ent_enterprise_setting` | 封面, 基本信息表, 表2 |
| `de_company_overview` | 表2(上半部分), 审计摘要.5 |
| `de_tech_indicator` | 表2(下半部分), 审计摘要.2, 表8, 表10, 图表C2/C7 |
| `de_energy_consumption` | 表7, 表9, 表11, 表14, 图表C1 |
| `de_energy_conversion` | 表15 |
| `de_product_unit_consumption` | 表21, 审计摘要.4, 图表C5 |
| `de_equipment_detail` | 表3, 表19 |
| `de_carbon_emission` | 表12, 表13, 审计摘要.3, 图表C4 |
| `de_energy_balance` | 表16, 图表C3 |
| `de_energy_flow` | 图表C6 (Sankey) |
| `de_five_year_target` | 附表(十四五目标) |
| `de_management_policy` | 表4 |
| `de_meter_instrument` | 表5 |
| `de_meter_config_rate` | 表6 |
| `de_product_output` | 表8(产量), 表10(产量单位) |
| `de_product_energy_cost` | 表17 |
| `de_saving_calculation` | 表18 |
| `de_tech_reform_history` | 表1 |
| `de_saving_project` | 附表(节能项目) |
| `de_obsolete_equipment` | 表20 |
| `de_saving_potential` | 表22a |
| `de_management_suggestion` | 表22b |
| `de_tech_reform_suggestion` | 表22c |
| `de_rectification` | 表22d |
| `de_report_text` | 34个文本段落 |
| `de_submission_field` | 审计人员信息 |
| `de_submission_table` | 审计人员列表 |
| `ar_report` | 封面(报告名/时间), 状态管理 |
| `ar_report_section` | 章节内容存储（可选） |

---

## 附录：能源流程图模块设计（C6 — AntV X6 画布流程图）

> 该模块为独立功能模块，开发完成后可复用于报告 2.4 节的图片导出。

### 功能定位
- 前端组件：`FlowEditor.vue`（已有骨架，基于 AntV X6）
- 位置：企业端"图表分析"菜单下，或在报告 2.4 节直接嵌入
- 数据来源：`de_energy_flow` 表

### 节点类型定义（6类）

| 节点类型 | 英文标识 | 颜色建议 | 说明 | 示例 |
|---|---|---|---|---|
| 外购能源 | `purchased` | 蓝色 #409EFF | 从外部购入的各类能源 | 电力、天然气、蒸汽、柴油 |
| 加工转换单元 | `conversion` | 橙色 #E6A23C | 能源加工转换设备/系统 | 锅炉、变压器、发电机 |
| 输送分配单元 | `distribution` | 绿色 #67C23A | 能源输送分配网络 | 配电系统、蒸汽管网、压缩空气管 |
| 终端使用单元 | `terminal` | 紫色 #909399 | 各车间/工序终端用能 | 生产车间、冷冻站、空压站 |
| 产品 | `product` | 红色 #F56C6C | 产品产出（含能源回收） | 成品、余热回收 |
| 非生产系统 | `non_production` | 灰色 #C0C4CC | 辅助/非生产用能 | 办公楼、照明、空调 |

### 数据库字段与流程图映射

| `de_energy_flow` 字段 | 流程图元素 | 说明 |
|---|---|---|
| `flow_stage` | 节点类型（上表6类之一） | 决定节点的颜色和图标 |
| `source_unit` | 连线起点节点名 | 自动创建或匹配已有节点 |
| `target_unit` | 连线终点节点名 | 自动创建或匹配已有节点 |
| `energy_product` | 连线标签（能源种类） | 显示在连线上 |
| `physical_quantity` | 连线标签（实物量） | 附加在连线标签上 |
| `standard_quantity` | 连线标签（标煤量） | 用于汇总计算 |
| `seq_no` | 排序 | 控制同类节点的排列顺序 |

### 布局规则
1. **从左到右**：外购能源 → 加工转换 → 输送分配 → 终端使用 → 产品/非生产
2. 同类型节点**垂直排列**
3. 连线上标注：`{energy_product}: {physical_quantity}{unit} ({standard_quantity}tce)`
4. 节点内显示：名称 + 该节点能源收支汇总值

### 导出与报告集成
- FlowEditor 提供"导出为图片"功能（AntV X6 支持 `graph.toDataURL()` / `graph.toPNG()`）
- 报告生成时（Wave 8），后端调用前端导出接口或使用 Puppeteer/Playwright 截图
- 导出的 PNG 图片插入 Word 报告的第 2.4 节"能源流程图"位置
