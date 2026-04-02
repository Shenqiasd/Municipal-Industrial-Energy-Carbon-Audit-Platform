-- H2 dev schema (MODE=MySQL compatible)
-- Core tables needed for development and CI.
-- Production uses MySQL with the full 55-table schema in /sql/

-- System user table
CREATE TABLE IF NOT EXISTS sys_user (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    username        VARCHAR(64)  NOT NULL,
    password        VARCHAR(128) NOT NULL,
    real_name       VARCHAR(64),
    phone           VARCHAR(20),
    email           VARCHAR(128),
    user_type       TINYINT      NOT NULL DEFAULT 0 COMMENT '0=enterprise,1=admin,2=auditor',
    enterprise_id   BIGINT,
    status          TINYINT      NOT NULL DEFAULT 1,
    password_changed TINYINT     NOT NULL DEFAULT 0,
    last_login_time  DATETIME,
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted      TINYINT      NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
);

-- System role table
CREATE TABLE IF NOT EXISTS sys_role (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    role_code   VARCHAR(64)  NOT NULL,
    role_name   VARCHAR(128) NOT NULL,
    user_type   TINYINT      NOT NULL DEFAULT 0,
    description VARCHAR(255),
    status      TINYINT      NOT NULL DEFAULT 1,
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted  TINYINT      NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_code (role_code)
);

-- User-role mapping
CREATE TABLE IF NOT EXISTS sys_user_role (
    id      BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_role (user_id, role_id)
);

-- Enterprise table
CREATE TABLE IF NOT EXISTS ent_enterprise (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    enterprise_code VARCHAR(64)  NOT NULL,
    enterprise_name VARCHAR(255) NOT NULL,
    credit_code     VARCHAR(64),
    industry_code   VARCHAR(32),
    province_code   VARCHAR(16),
    city_code       VARCHAR(16),
    address         VARCHAR(512),
    contact_name    VARCHAR(64),
    contact_phone   VARCHAR(20),
    audit_year      INT          NOT NULL DEFAULT 2024,
    status          TINYINT      NOT NULL DEFAULT 1,
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted      TINYINT      NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_enterprise_code (enterprise_code)
);

-- Energy type base data
CREATE TABLE IF NOT EXISTS bs_energy (
    id           BIGINT         NOT NULL AUTO_INCREMENT,
    energy_code  VARCHAR(32)    NOT NULL,
    energy_name  VARCHAR(128)   NOT NULL,
    energy_unit  VARCHAR(32),
    convert_coef DECIMAL(18, 6) DEFAULT 1.0,
    co2_factor   DECIMAL(18, 6) DEFAULT 0.0,
    category     VARCHAR(32),
    sort_order   INT            DEFAULT 0,
    status       TINYINT        NOT NULL DEFAULT 1,
    create_time  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted   TINYINT        NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_energy_code (energy_code)
);

-- Product base data
CREATE TABLE IF NOT EXISTS bs_product (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    product_code  VARCHAR(64)  NOT NULL,
    product_name  VARCHAR(255) NOT NULL,
    product_unit  VARCHAR(32),
    industry_code VARCHAR(32),
    sort_order    INT          DEFAULT 0,
    status        TINYINT      NOT NULL DEFAULT 1,
    create_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted    TINYINT      NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_product_code (product_code)
);

-- Unit base data
CREATE TABLE IF NOT EXISTS bs_unit (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    unit_code   VARCHAR(32) NOT NULL,
    unit_name   VARCHAR(128) NOT NULL,
    unit_symbol VARCHAR(32),
    category    VARCHAR(32),
    sort_order  INT         DEFAULT 0,
    status      TINYINT     NOT NULL DEFAULT 1,
    create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted  TINYINT     NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_unit_code (unit_code)
);

-- Template table
CREATE TABLE IF NOT EXISTS tpl_template (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    template_code VARCHAR(64)  NOT NULL,
    template_name VARCHAR(255) NOT NULL,
    template_type VARCHAR(32),
    industry_code VARCHAR(32),
    audit_year    INT,
    file_path     VARCHAR(512),
    description   VARCHAR(512),
    status        TINYINT      NOT NULL DEFAULT 1,
    create_by     BIGINT,
    create_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted    TINYINT      NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_template_code (template_code)
);
