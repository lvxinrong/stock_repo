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
)