-- H2 dev seed data: initial admin account
-- Password is BCrypt of 'admin123'
-- Only applied when using H2 (dev profile on Replit / CI)

MERGE INTO sys_user (id, username, password, real_name, user_type, status, password_changed, create_by)
KEY(username)
VALUES (
    1,
    'admin',
    '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2',
    '系统管理员',
    1,
    1,
    0,
    'system'
);
