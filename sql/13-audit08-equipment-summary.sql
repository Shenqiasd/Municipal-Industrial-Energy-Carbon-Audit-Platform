-- Audit08: 主要耗能设备状况表 — schema expansion
-- Adds missing columns to de_equipment_summary for full Excel mapping

-- 1. New column: energy_efficiency_level (Excel col J: 设备对标情况/能效级别)
ALTER TABLE de_equipment_summary
ADD COLUMN energy_efficiency_level VARCHAR(128) DEFAULT NULL
COMMENT '设备对标情况(能效级别)' AFTER install_location;

-- 2. New column: submission_id (required for extraction pipeline keying)
ALTER TABLE de_equipment_summary
ADD COLUMN submission_id BIGINT DEFAULT NULL
COMMENT '关联提交ID -> tpl_submission.id' AFTER audit_year;

-- 3. Index on submission_id for efficient lookups
ALTER TABLE de_equipment_summary
ADD INDEX idx_submission_id (submission_id);
