package com.lv.score.ScoreModel.calculate.entity;

import lombok.Data;

@Data
public class CalculateResultMonth {

    // 股票代码
    private String ts_code;
    // 指数代码
    private String index_code;
    //股票名称
    private String stock_name;
    // 指数名称
    private String index_code_name;
    // 分数计算月份
    private String tradeDateMonth;
    // 分数
    private Double score;
    // 本月收益率
    private String yieldRate;
    // 计算月份涨幅
    private String tradeDateYieldRate;
}
