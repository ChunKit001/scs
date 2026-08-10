CREATE TABLE IF NOT EXISTS customer (
    customer_id         VARCHAR(64)  NOT NULL COMMENT '客户ID',
    member_id           VARCHAR(64)           DEFAULT NULL COMMENT '会员ID',
    global_id           VARCHAR(64)           DEFAULT NULL COMMENT '全局ID',
    registered_capital  BIGINT       NOT NULL DEFAULT 0 COMMENT '注册资本',
    company_name        VARCHAR(255)          DEFAULT NULL COMMENT '公司名',
    created_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (customer_id),
    KEY idx_customer_company_name (company_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表';
