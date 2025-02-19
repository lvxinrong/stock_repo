create table index_basic_daily (
    ts_code varchar(100) comment '股票代码',
    trade_date varchar(100) comment '交易日期',
    open float comment '开盘价',
    high float comment '最高价',
    low float comment '	最低价',
    close float comment '收盘价',
    pre_close float comment '昨收价除权价，前复权',
    change_value float comment '涨跌额，tushare原字段 change',
    pct_chg float comment '涨跌幅 基于除权后的昨收计算的涨跌幅：（今收-除权昨收）/除权昨收 ',
    vol float comment '成交量 单位 手',
    amount float comment '成交额 单位 千元'
);

create table trade_daily (
    ts_code varchar(100) comment '股票代码',
    trade_date varchar(100) comment '交易日期',
    open float comment '开盘价',
    high float comment '最高价',
    low float comment '	最低价',
    close float comment '收盘价',
    pre_close float comment '昨收价除权价，前复权',
    change_value float comment '涨跌额，tushare原字段 change',
    pct_chg float comment '涨跌幅 基于除权后的昨收计算的涨跌幅：（今收-除权昨收）/除权昨收 ',
    vol float comment '成交量 单位 手',
    amount float comment '成交额 单位 千元'
);

create table cyz_100_stock
(
    index_code varchar(100) comment '指数代码',
    con_code varchar(100) comment '成分代码',
    trade_date varchar(100) comment '交易日期',
    weight float comment '权重'
);

CREATE TABLE daily_basic_info (
    ts_code VARCHAR(100) NOT NULL COMMENT 'TS股票代码',
    trade_date VARCHAR(100) NOT NULL COMMENT '交易日期',
    close FLOAT COMMENT '当日收盘价',
    turnover_rate FLOAT COMMENT '换手率（%）',
    turnover_rate_f FLOAT COMMENT '换手率（自由流通股）',
    volume_ratio FLOAT COMMENT '量比',
    pe FLOAT COMMENT '市盈率（总市值/净利润，亏损的PE为空）',
    pe_ttm FLOAT COMMENT '市盈率（TTM，亏损的PE为空）',
    pb FLOAT COMMENT '市净率（总市值/净资产）',
    ps FLOAT COMMENT '市销率',
    ps_ttm FLOAT COMMENT '市销率（TTM）',
    dv_ratio FLOAT COMMENT '股息率（%）',
    dv_ttm FLOAT COMMENT '股息率（TTM）（%）',
    total_share FLOAT COMMENT '总股本（万股）',
    float_share FLOAT COMMENT '流通股本（万股）',
    free_share FLOAT COMMENT '自由流通股本（万）',
    total_mv FLOAT COMMENT '总市值（万元）',
    circ_mv FLOAT COMMENT '流通市值（万元）'
) COMMENT = '股票交易及财务数据';

create table hs_300_stock
(
    index_code varchar(100) comment '指数代码',
    con_code varchar(100) comment '成分代码',
    trade_date varchar(100) comment '交易日期',
    weight float comment '权重'
);

create table zh_100_stock
(
    index_code varchar(100) comment '指数代码',
    con_code varchar(100) comment '成分代码',
    trade_date varchar(100) comment '交易日期',
    weight float comment '权重'
);

create table zh_500_stock
(
    index_code varchar(100) comment '指数代码',
    con_code varchar(100) comment '成分代码',
    trade_date varchar(100) comment '交易日期',
    weight float comment '权重'
);

create table zh_800_stock
(
    index_code varchar(100) comment '指数代码',
    con_code varchar(100) comment '成分代码',
    trade_date varchar(100) comment '交易日期',
    weight float comment '权重'
);

create table zh_1000_stock
(
    index_code varchar(100) comment '指数代码',
    con_code varchar(100) comment '成分代码',
    trade_date varchar(100) comment '交易日期',
    weight float comment '权重'
);