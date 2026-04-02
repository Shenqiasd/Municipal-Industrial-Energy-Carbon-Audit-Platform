-- H2 dev schema: minimal tables required for Wave 0 auth + mapper registration
-- Production uses MySQL with full schema.sql

CREATE TABLE IF NOT EXISTS sys_user (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(64)  NOT NULL,
    password        VARCHAR(128) NOT NULL,
    real_name       VARCHAR(64),
    phone           VARCHAR(20),
    email           VARCHAR(128),
    user_type       INT          NOT NULL DEFAULT 1,
    enterprise_id   BIGINT,
    status          INT          NOT NULL DEFAULT 1,
    last_login_time TIMESTAMP,
    password_changed INT         NOT NULL DEFAULT 0,
    create_by       VARCHAR(64),
    create_time     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    update_by       VARCHAR(64),
    update_time     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    deleted         INT          NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS sys_role (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name   VARCHAR(64)  NOT NULL,
    role_key    VARCHAR(64)  NOT NULL,
    order_num   INT          DEFAULT 0,
    status      INT          NOT NULL DEFAULT 1,
    remark      VARCHAR(256),
    create_by   VARCHAR(64),
    create_time TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    update_by   VARCHAR(64),
    update_time TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    deleted     INT          NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS ent_enterprise (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(128) NOT NULL,
    credit_code    VARCHAR(64),
    industry_type  VARCHAR(64),
    province       VARCHAR(32),
    city           VARCHAR(32),
    district       VARCHAR(32),
    address        VARCHAR(256),
    contact_person VARCHAR(64),
    contact_phone  VARCHAR(20),
    contact_email  VARCHAR(128),
    status         INT          NOT NULL DEFAULT 1,
    remark         VARCHAR(512),
    create_by      VARCHAR(64),
    create_time    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    update_by      VARCHAR(64),
    update_time    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    deleted        INT          NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS bs_energy (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    energy_code       VARCHAR(64),
    energy_name       VARCHAR(64)  NOT NULL,
    category          VARCHAR(64),
    unit              VARCHAR(32),
    conversion_factor DECIMAL(18,6),
    carbon_factor     DECIMAL(18,6),
    order_num         INT          DEFAULT 0,
    status            INT          NOT NULL DEFAULT 1,
    create_by         VARCHAR(64),
    create_time       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    update_by         VARCHAR(64),
    update_time       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    deleted           INT          NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS bs_product (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_code VARCHAR(64),
    product_name VARCHAR(64)  NOT NULL,
    category     VARCHAR(64),
    unit         VARCHAR(32),
    order_num    INT          DEFAULT 0,
    status       INT          NOT NULL DEFAULT 1,
    remark       VARCHAR(512),
    create_by    VARCHAR(64),
    create_time  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    update_by    VARCHAR(64),
    update_time  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    deleted      INT          NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS bs_unit (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    unit_code         VARCHAR(64),
    unit_name         VARCHAR(64)  NOT NULL,
    symbol            VARCHAR(32),
    category          VARCHAR(64),
    conversion_factor DECIMAL(18,6),
    order_num         INT          DEFAULT 0,
    status            INT          NOT NULL DEFAULT 1,
    create_by         VARCHAR(64),
    create_time       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    update_by         VARCHAR(64),
    update_time       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    deleted           INT          NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS tpl_template (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    template_code    VARCHAR(64)  NOT NULL,
    template_name    VARCHAR(128) NOT NULL,
    category         VARCHAR(64),
    description      VARCHAR(512),
    current_version  INT          DEFAULT 0,
    status           INT          NOT NULL DEFAULT 0,
    remark           VARCHAR(512),
    create_by        VARCHAR(64),
    create_time      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    update_by        VARCHAR(64),
    update_time      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    deleted          INT          NOT NULL DEFAULT 0
);
