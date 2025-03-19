create table broker_recommend
(
    month   varchar(100) null,
    broker  varchar(100) null,
    ts_code varchar(100) null,
    name    varchar(100) null
);

create table cyz_100_stock
(
    index_code varchar(100) null comment '指数代码',
    con_code   varchar(100) null comment '成分代码',
    trade_date varchar(100) null comment '交易日期',
    weight     float        null comment '权重'
);

create table daily_basic_info
(
    ts_code         varchar(100) not null comment 'TS股票代码',
    trade_date      varchar(100) not null comment '交易日期',
    close           float        null comment '当日收盘价',
    turnover_rate   float        null comment '换手率（%）',
    turnover_rate_f float        null comment '换手率（自由流通股）',
    volume_ratio    float        null comment '量比',
    pe              float        null comment '市盈率（总市值/净利润，亏损的PE为空）',
    pe_ttm          float        null comment '市盈率（TTM，亏损的PE为空）',
    pb              float        null comment '市净率（总市值/净资产）',
    ps              float        null comment '市销率',
    ps_ttm          float        null comment '市销率（TTM）',
    dv_ratio        float        null comment '股息率（%）',
    dv_ttm          float        null comment '股息率（TTM）（%）',
    total_share     float        null comment '总股本（万股）',
    float_share     float        null comment '流通股本（万股）',
    free_share      float        null comment '自由流通股本（万）',
    total_mv        float        null comment '总市值（万元）',
    circ_mv         float        null comment '流通市值（万元）',
    primary key (ts_code, trade_date)
)
    comment '股票交易及财务数据';

create table hs_300_stock
(
    index_code varchar(100) not null comment '指数代码',
    con_code   varchar(100) not null comment '成分代码',
    trade_date varchar(100) not null comment '交易日期',
    weight     float        null comment '权重',
    primary key (trade_date, index_code, con_code)
);

create table index_basic
(
    ts_code    varchar(100) null,
    name       varchar(100) null,
    market     varchar(100) null,
    publisher  varchar(100) null,
    category   varchar(100) null,
    base_date  varchar(100) null,
    base_point varchar(100) null,
    list_date  varchar(100) null
);

create table index_basic_daily
(
    ts_code      varchar(100) not null comment '股票代码',
    trade_date   varchar(100) not null comment '交易日期',
    open         float        null comment '开盘价',
    high         float        null comment '最高价',
    low          float        null comment '	最低价',
    close        float        null comment '收盘价',
    pre_close    float        null comment '昨收价除权价，前复权',
    change_value float        null comment '涨跌额，tushare原字段 change',
    pct_chg      float        null comment '涨跌幅 基于除权后的昨收计算的涨跌幅：（今收-除权昨收）/除权昨收 ',
    vol          float        null comment '成交量 单位 手',
    amount       float        null comment '成交额 单位 千元',
    primary key (ts_code, trade_date)
);

create table limit_down_ths_daily
(
    trade_date        varchar(100) null,
    ts_code           varchar(100) null,
    name              varchar(100) null,
    price             varchar(100) null,
    pct_chg           varchar(100) null,
    open_num          varchar(100) null,
    lu_desc           varchar(100) null,
    limit_type        varchar(100) null,
    tag               varchar(100) null,
    status            varchar(100) null,
    limit_order       varchar(100) null,
    limit_amount      varchar(100) null,
    turnover_rate     varchar(100) null,
    free_float        varchar(100) null,
    lu_limit_order    varchar(100) null,
    limit_up_suc_rate varchar(100) null,
    turnover          varchar(100) null,
    market_type       varchar(100) null
);

create table limit_up_deepseek_analyze_result
(
    ts_code        varchar(100)  not null comment '股票代码',
    trade_date     varchar(100)  not null comment '交易日期',
    analyze_result varchar(1000) null comment '分析结果',
    score          varchar(100)  null comment 'deepseek打分',
    primary key (trade_date, ts_code)
);

create table limit_up_ths_daily
(
    trade_date        varchar(100) not null,
    ts_code           varchar(100) not null,
    name              varchar(100) null,
    price             varchar(100) null,
    pct_chg           varchar(100) null,
    open_num          varchar(100) null,
    lu_desc           varchar(100) null,
    limit_type        varchar(100) null,
    tag               varchar(100) null,
    status            varchar(100) null,
    limit_order       varchar(100) null,
    limit_amount      varchar(100) null,
    turnover_rate     varchar(100) null,
    free_float        varchar(100) null,
    lu_limit_order    varchar(100) null,
    limit_up_suc_rate varchar(100) null,
    turnover          varchar(100) null,
    market_type       varchar(100) null,
    primary key (trade_date, ts_code)
);

create table stock_basic
(
    ts_code     varchar(100) not null
        primary key,
    symbol      varchar(100) null,
    name        varchar(100) null,
    area        varchar(100) null,
    industry    varchar(100) null,
    fullname    varchar(100) null,
    enname      varchar(100) null,
    cnspell     varchar(100) null,
    market      varchar(100) null,
    exchange    varchar(100) null,
    curr_type   varchar(100) null,
    list_status varchar(100) null,
    list_date   varchar(100) null,
    delist_date varchar(100) null,
    is_hs       varchar(100) null
);

create table stock_money_flow_day
(
    id              int unsigned auto_increment
        primary key,
    trade_date      varchar(100)   not null comment '交易日期',
    ts_code         varchar(100)   not null comment 'TS代码',
    buy_sm_vol      int            null comment '小单买入量（手）',
    buy_sm_amount   decimal(12, 2) null comment '小单买入金额（万元）',
    sell_sm_vol     int            null comment '小单卖出量（手）',
    sell_sm_amount  decimal(12, 2) null comment '小单卖出金额（万元）',
    buy_md_vol      int            null comment '中单买入量（手）',
    buy_md_amount   decimal(12, 2) null comment '中单买入金额（万元）',
    sell_md_vol     int            null comment '中单卖出量（手）',
    sell_md_amount  decimal(12, 2) null comment '中单卖出金额（万元）',
    buy_lg_vol      int            null comment '大单买入量（手）',
    buy_lg_amount   decimal(12, 2) null comment '大单买入金额（万元）',
    sell_lg_vol     int            null comment '大单卖出量（手）',
    sell_lg_amount  decimal(12, 2) null comment '大单卖出金额（万元）',
    buy_elg_vol     int            null comment '特大单买入量（手）',
    buy_elg_amount  decimal(12, 2) null comment '特大单买入金额（万元）',
    sell_elg_vol    int            null comment '特大单卖出量（手）',
    sell_elg_amount decimal(12, 2) null comment '特大单卖出金额（万元）',
    net_mf_vol      int            null comment '净流入量（手）',
    net_mf_amount   decimal(12, 2) null comment '净流入额（万元）',
    turnover_rate   decimal(5, 2)  null comment '换手率(%)',
    constraint unique_record
        unique (trade_date, ts_code)
)
    comment '个股资金流向表' collate = utf8mb4_general_ci;

create index idx_trade_date
    on stock_money_flow_day (trade_date);

create index idx_ts_code
    on stock_money_flow_day (ts_code);

create table ths_index
(
    ts_code   varchar(100) null,
    name      varchar(100) null,
    count     varchar(100) null,
    exchange  varchar(100) null,
    list_date varchar(100) null,
    type      varchar(100) null
);

create table trade_daily
(
    ts_code      varchar(100) not null comment '股票代码',
    trade_date   varchar(100) not null comment '交易日期',
    open         float        null comment '开盘价',
    high         float        null comment '最高价',
    low          float        null comment '	最低价',
    close        float        null comment '收盘价',
    pre_close    float        null comment '昨收价除权价，前复权',
    change_value float        null comment '涨跌额，tushare原字段 change',
    pct_chg      float        null comment '涨跌幅 基于除权后的昨收计算的涨跌幅：（今收-除权昨收）/除权昨收 ',
    vol          float        null comment '成交量 单位 手',
    amount       float        null comment '成交额 单位 千元',
    primary key (trade_date, ts_code)
);

create table zh_1000_stock
(
    index_code varchar(100) not null comment '指数代码',
    con_code   varchar(100) not null comment '成分代码',
    trade_date varchar(100) not null comment '交易日期',
    weight     float        null comment '权重',
    primary key (index_code, con_code, trade_date)
);

create table zh_100_stock
(
    index_code varchar(100) not null comment '指数代码',
    con_code   varchar(100) not null comment '成分代码',
    trade_date varchar(100) not null comment '交易日期',
    weight     float        null comment '权重',
    primary key (trade_date, con_code, index_code)
);

create table zh_500_stock
(
    index_code varchar(100) not null comment '指数代码',
    con_code   varchar(100) not null comment '成分代码',
    trade_date varchar(100) not null comment '交易日期',
    weight     float        null comment '权重',
    primary key (index_code, con_code, trade_date)
);

create table zh_800_stock
(
    index_code varchar(100) not null comment '指数代码',
    con_code   varchar(100) not null comment '成分代码',
    trade_date varchar(100) not null comment '交易日期',
    weight     float        null comment '权重',
    primary key (trade_date, con_code, index_code)
);

CREATE TABLE `stock_money_flow_ths_day` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `trade_date` varchar(100) NOT NULL COMMENT '交易日期',
  `ts_code` varchar(100) NOT NULL COMMENT '股票代码',
  `name` varchar(100) NOT NULL COMMENT '股票名称',
  `pct_change` decimal(5,2) NOT NULL COMMENT '涨跌幅(%)',
  `latest` decimal(10,2) NOT NULL COMMENT '最新价',
  `net_amount` decimal(12,2) NOT NULL COMMENT '资金净流入(万元)',
  `net_d5_amount` decimal(12,2) NOT NULL COMMENT '5日主力净额(万元)',
  `buy_lg_amount` decimal(12,2) NOT NULL COMMENT '大单净流入额(万元)',
  `buy_lg_amount_rate` decimal(5,2) NOT NULL COMMENT '大单净流入占比(%)',
  `buy_md_amount` decimal(12,2) NOT NULL COMMENT '中单净流入额(万元)',
  `buy_md_amount_rate` decimal(5,2) NOT NULL COMMENT '中单净流入占比(%)',
  `buy_sm_amount` decimal(12,2) NOT NULL COMMENT '小单净流入额(万元)',
  `buy_sm_amount_rate` decimal(5,2) NOT NULL COMMENT '小单净流入占比(%)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_record` (`trade_date`,`ts_code`),
  KEY `idx_trade_date` (`trade_date`),
  KEY `idx_ts_code` (`ts_code`),
  KEY `idx_name` (`name`(10))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='个股资金流向明细表';

CREATE TABLE `stock_money_flow_dc_day` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `trade_date` varchar(100) NOT NULL COMMENT '交易日期',
  `ts_code` varchar(100) NOT NULL COMMENT '股票代码',
  `name` varchar(100) NOT NULL COMMENT '股票名称',
  `pct_change` DECIMAL(6,2) NOT NULL COMMENT '涨跌幅(%)',
  `close` DECIMAL(10,2) NOT NULL COMMENT '收盘价',
  `net_amount` DECIMAL(12,2) NOT NULL COMMENT '主力净流入额(万元)',
  `net_amount_rate` DECIMAL(5,2) NOT NULL COMMENT '主力净流入占比(%)',
  `buy_elg_amount` DECIMAL(12,2) NOT NULL COMMENT '超大单净流入额(万元)',
  `buy_elg_amount_rate` DECIMAL(5,2) NOT NULL COMMENT '超大单净流入占比(%)',
  `buy_lg_amount` DECIMAL(12,2) NOT NULL COMMENT '大单净流入额(万元)',
  `buy_lg_amount_rate` DECIMAL(5,2) NOT NULL COMMENT '大单净流入占比(%)',
  `buy_md_amount` DECIMAL(12,2) NOT NULL COMMENT '中单净流入额(万元)',
  `buy_md_amount_rate` DECIMAL(5,2) NOT NULL COMMENT '中单净流入占比(%)',
  `buy_sm_amount` DECIMAL(12,2) NOT NULL COMMENT '小单净流入额(万元)',
  `buy_sm_amount_rate` DECIMAL(5,2) NOT NULL COMMENT '小单净流入占比(%)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_trade_stock` (`trade_date`,`ts_code`),
  KEY `idx_trade_date` (`trade_date`),
  KEY `idx_ts_code` (`ts_code`),
  KEY `idx_name` (`name`(10))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='个股每日资金流向明细表';

CREATE TABLE `ind_money_flow_ths_day` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `trade_date` VARCHAR(100) NOT NULL COMMENT '交易日期',
  `ts_code` VARCHAR(100) NOT NULL COMMENT '板块代码',
  `industry` VARCHAR(100) NOT NULL COMMENT '板块名称',
  `lead_stock` VARCHAR(200) NOT NULL COMMENT '领涨股票名称',
  `close` DECIMAL(12,4) NOT NULL COMMENT '收盘指数',
  `pct_change` DECIMAL(7,4) NOT NULL COMMENT '指数涨跌幅(%)',
  `company_num` INT UNSIGNED NOT NULL COMMENT '公司数量',
  `pct_change_stock` DECIMAL(7,4) NOT NULL COMMENT '领涨股涨跌幅(%)',
  `close_price` DECIMAL(10,2) NOT NULL COMMENT '领涨股最新价',
  `net_buy_amount` DECIMAL(16,4) NOT NULL COMMENT '流入资金(亿元)',
  `net_sell_amount` DECIMAL(16,4) NOT NULL COMMENT '流出资金(亿元)',
  `net_amount` DECIMAL(20,2) NOT NULL COMMENT '净额(元)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_date_industry` (`trade_date`, `ts_code`),
  KEY `idx_trade_date` (`trade_date`),
  KEY `idx_industry` (`industry`(15)),
  KEY `idx_ts_code` (`ts_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='板块资金流向(同花顺)';

CREATE TABLE `ind_money_flow_dc_day`
(
    `id`                  INT UNSIGNED      NOT NULL AUTO_INCREMENT,
    `trade_date`          VARCHAR(100)             NOT NULL COMMENT '交易日期',
    `content_type`        VARCHAR(100)       NOT NULL COMMENT '数据类型',
    `ts_code`             VARCHAR(100)       NOT NULL COMMENT 'DC板块代码',
    `name`                VARCHAR(100)       NOT NULL COMMENT '板块名称',
    `pct_change`          DECIMAL(7, 4)     NOT NULL COMMENT '板块涨跌幅(%)',
    `close`               DECIMAL(12, 4)    NOT NULL COMMENT '板块最新指数',
    `net_amount`          DECIMAL(20, 2)    NOT NULL COMMENT '主力净流入净额(元)',
    `net_amount_rate`     DECIMAL(5, 2)     NOT NULL COMMENT '主力净流入净占比(%)',
    `buy_elg_amount`      DECIMAL(20, 2)    NOT NULL COMMENT '超大单净流入净额(元)',
    `buy_elg_amount_rate` DECIMAL(5, 2)     NOT NULL COMMENT '超大单净流入净占比(%)',
    `buy_lg_amount`       DECIMAL(20, 2)    NOT NULL COMMENT '大单净流入净额(元)',
    `buy_lg_amount_rate`  DECIMAL(5, 2)     NOT NULL COMMENT '大单净流入净占比(%)',
    `buy_md_amount`       DECIMAL(20, 2)    NOT NULL COMMENT '中单净流入净额(元)',
    `buy_md_amount_rate`  DECIMAL(5, 2)     NOT NULL COMMENT '中单净流入净占比(%)',
    `buy_sm_amount`       DECIMAL(20, 2)    NOT NULL COMMENT '小单净流入净额(元)',
    `buy_sm_amount_rate`  DECIMAL(5, 2)     NOT NULL COMMENT '小单净流入净占比(%)',
    `buy_sm_amount_stock` VARCHAR(5000)      NOT NULL COMMENT '主力净流入最大股',
    `rank_value`                INT UNSIGNED NOT NULL COMMENT '序号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_sector_flow` (`trade_date`, `ts_code`, `content_type`),
    KEY `idx_date` (`trade_date`),
    KEY `idx_ts_code` (`ts_code`),
    KEY `idx_rank` (`rank`),
    KEY `idx_name` (`name`(15))
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
COMMENT='板块资金流向（DC）';

CREATE TABLE `mkt_money_flow_dc_day` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `trade_date` varchar(100) NOT NULL COMMENT '交易日期',
  `close_sh` DECIMAL(10,4) NOT NULL COMMENT '上证收盘价(点)',
  `pct_change_sh` DECIMAL(6,4) NOT NULL COMMENT '上证涨跌幅(%)',
  `close_sz` DECIMAL(10,4) NOT NULL COMMENT '深证收盘价(点)',
  `pct_change_sz` DECIMAL(6,4) NOT NULL COMMENT '深证涨跌幅(%)',
  `net_amount` DECIMAL(20,2) NOT NULL COMMENT '主力净流入净额(元)',
  `net_amount_rate` DECIMAL(5,2) NOT NULL COMMENT '主力净流入净占比(%)',
  `buy_elg_amount` DECIMAL(20,2) NOT NULL COMMENT '超大单净流入净额(元)',
  `buy_elg_amount_rate` DECIMAL(5,2) NOT NULL COMMENT '超大单净流入净占比(%)',
  `buy_lg_amount` DECIMAL(20,2) NOT NULL COMMENT '大单净流入净额(元)',
  `buy_lg_amount_rate` DECIMAL(5,2) NOT NULL COMMENT '大单净流入净占比(%)',
  `buy_md_amount` DECIMAL(20,2) NOT NULL COMMENT '中单净流入净额(元)',
  `buy_md_amount_rate` DECIMAL(5,2) NOT NULL COMMENT '中单净流入净占比(%)',
  `buy_sm_amount` DECIMAL(20,2) NOT NULL COMMENT '小单净流入净额(元)',
  `buy_sm_amount_rate` DECIMAL(5,2) NOT NULL COMMENT '小单净流入净占比(%)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_trade_date` (`trade_date`),
  KEY `idx_close_sh` (`close_sh`),
  KEY `idx_pct_change` (`pct_change_sh`,`pct_change_sz`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='大盘资金流向（DC）';


CREATE TABLE stock_cyq_perf_day (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    ts_code VARCHAR(100) NOT NULL COMMENT '股票代码',
    trade_date VARCHAR(100) NOT NULL COMMENT '交易日期',
    his_low DECIMAL(10, 2) NOT NULL COMMENT '历史最低价',
    his_high DECIMAL(10, 2) NOT NULL COMMENT '历史最高价',
    cost_5pct DECIMAL(10, 2) NOT NULL COMMENT '5分位成本',
    cost_15pct DECIMAL(10, 2) NOT NULL COMMENT '15分位成本',
    cost_50pct DECIMAL(10, 2) NOT NULL COMMENT '50分位成本',
    cost_85pct DECIMAL(10, 2) NOT NULL COMMENT '85分位成本',
    cost_95pct DECIMAL(10, 2) NOT NULL COMMENT '95分位成本',
    weight_avg DECIMAL(10, 2) NOT NULL COMMENT '加权平均成本',
    winner_rate DECIMAL(10, 2) NOT NULL COMMENT '胜率',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY unique_stock_date (ts_code, trade_date) COMMENT '股票代码和交易日期唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='每日筹码及胜率';

CREATE TABLE stock_stk_factor_data
(
    ts_code       VARCHAR(100)   NOT NULL COMMENT '股票代码',
    trade_date    VARCHAR(100)   NOT NULL COMMENT '交易日期',
    close         DECIMAL(20, 2)  COMMENT '收盘价',
    open          DECIMAL(20, 2)  COMMENT '开盘价',
    high          DECIMAL(20, 2)  COMMENT '最高价',
    low           DECIMAL(20, 2)  COMMENT '最低价',
    pre_close     DECIMAL(20, 2)  COMMENT '昨收价',
    change_value  DECIMAL(20, 2)  COMMENT '涨跌额',
    pct_change    DECIMAL(20, 2)  COMMENT '涨跌幅',
    vol           DECIMAL(20, 2)  COMMENT '成交量 （手）',
    amount        DECIMAL(20, 2)  COMMENT '成交额 （千元）',
    adj_factor    DECIMAL(20, 6)  COMMENT '复权因子',
    open_hfq      DECIMAL(20, 2)  COMMENT '开盘价后复权',
    open_qfq      DECIMAL(20, 2)  COMMENT '开盘价前复权',
    close_hfq     DECIMAL(20, 2)  COMMENT '收盘价后复权',
    close_qfq     DECIMAL(20, 2)  COMMENT '收盘价前复权',
    high_hfq      DECIMAL(20, 2)  COMMENT '最高价后复权',
    high_qfq      DECIMAL(20, 2)  COMMENT '最高价前复权',
    low_hfq       DECIMAL(20, 2)  COMMENT '最低价后复权',
    low_qfq       DECIMAL(20, 2)  COMMENT '最低价前复权',
    pre_close_hfq DECIMAL(20, 2)  COMMENT '昨收价后复权',
    pre_close_qfq DECIMAL(20, 2)  COMMENT '昨收价前复权',
    macd_dif      DECIMAL(20, 2)  COMMENT 'MCAD_DIF (基于前复权价格计算，下同)',
    macd_dea      DECIMAL(20, 2)  COMMENT 'MCAD_DEA',
    macd          DECIMAL(20, 2)  COMMENT 'MCAD',
    kdj_k         DECIMAL(20, 2)  COMMENT 'KDJ_K',
    kdj_d         DECIMAL(20, 2)  COMMENT 'KDJ_D',
    kdj_j         DECIMAL(20, 2)  COMMENT 'KDJ_J',
    rsi_6         DECIMAL(20, 2)  COMMENT 'RSI_6',
    rsi_12        DECIMAL(20, 2)  COMMENT 'RSI_12',
    rsi_24        DECIMAL(20, 2)  COMMENT 'RSI_24',
    boll_upper    DECIMAL(20, 2)  COMMENT 'BOLL_UPPER',
    boll_mid      DECIMAL(20, 2)  COMMENT 'BOLL_MID',
    boll_lower    DECIMAL(20, 2)  COMMENT 'BOLL_LOWER',
    cci           DECIMAL(20, 2)  COMMENT 'CCI',
    PRIMARY KEY (ts_code, trade_date)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='股票技术因子';