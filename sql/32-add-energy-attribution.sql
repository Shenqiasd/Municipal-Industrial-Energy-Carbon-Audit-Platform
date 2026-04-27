-- GRA-39: 能源品种新增归属字段
ALTER TABLE bs_energy
  ADD COLUMN IF NOT EXISTS attribution VARCHAR(20) DEFAULT NULL
  COMMENT '归属类型: 化石燃料/非化石燃料';

ALTER TABLE bs_energy_catalog
  ADD COLUMN IF NOT EXISTS attribution VARCHAR(20) DEFAULT NULL
  COMMENT '归属类型: 化石燃料/非化石燃料';
