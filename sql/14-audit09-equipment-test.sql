-- Audit09: 重点设备测试数据 — 添加 submission_id 列
-- de_equipment_test 表已存在，仅需补充 submission_id

ALTER TABLE de_equipment_test
  ADD COLUMN submission_id BIGINT DEFAULT NULL
  COMMENT '关联提交ID -> tpl_submission.id'
  AFTER audit_year;

ALTER TABLE de_equipment_test
  ADD INDEX idx_submission_id (submission_id);
