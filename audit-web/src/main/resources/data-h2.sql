-- H2 dev seed data
-- Seed account: admin / admin123
-- BCrypt hash of "admin123" (cost=10)

MERGE INTO sys_user (
    id, username, password, real_name, phone, email,
    user_type, enterprise_id, status, password_changed,
    create_time, update_time, is_deleted
) KEY (username) VALUES (
    1,
    'admin',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaIW0LIdnBGOS',
    '系统管理员',
    '13800000000',
    'admin@energy-audit.com',
    1,
    NULL,
    1,
    0,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    0
);

MERGE INTO sys_role (
    id, role_code, role_name, user_type, description,
    status, create_time, update_time, is_deleted
) KEY (role_code) VALUES
    (1, 'ROLE_ADMIN',      '系统管理员', 1, '平台超级管理员', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    (2, 'ROLE_ENTERPRISE',  '企业用户',  0, '企业端普通用户', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    (3, 'ROLE_AUDITOR',     '审核员',    2, '能源审计员',     1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

MERGE INTO sys_user_role (id, user_id, role_id) KEY (user_id, role_id)
VALUES (1, 1, 1);
