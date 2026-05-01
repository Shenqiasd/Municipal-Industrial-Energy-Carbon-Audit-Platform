-- Rework the (enterprise_id, audit_year, report_type) uniqueness constraint added in
-- sql/34 so it only applies to LIVE rows (deleted = 0). The original key included
-- `deleted` as the fourth column, but `deleted` is effectively boolean (0/1), so the
-- moment a soft-deleted row exists for some (ent, year, type) you can never create
-- a second soft-deleted row for the same triple — the next soft-delete fails with
-- a duplicate-key error.
--
-- MySQL trick: a virtual generated column that is NULL when deleted=1 and 0 when
-- deleted=0. Indexes with multiple NULL values are allowed in MySQL UNIQUE indexes,
-- so unbounded soft-deleted rows can coexist while still keeping at most one live
-- row per (enterprise_id, audit_year, report_type).

ALTER TABLE ar_report
    DROP INDEX uk_ar_report_ent_year_type;

ALTER TABLE ar_report
    ADD COLUMN deleted_uniq_marker TINYINT
        GENERATED ALWAYS AS (IF(deleted = 0, 0, NULL)) VIRTUAL
        COMMENT '配合 uk_ar_report_live_ent_year_type 使用：deleted=0 时为 0；deleted=1 时为 NULL，允许多软删行共存';

ALTER TABLE ar_report
    ADD UNIQUE KEY uk_ar_report_live_ent_year_type
        (enterprise_id, audit_year, report_type, deleted_uniq_marker);
