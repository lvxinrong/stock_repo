package com.lv.score.ScoreModel.stock_strategy.super_trend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuperTrendTradeSignal {

    private String tsCode;
    private String tradeDate;
    private String action;
    private double closePrice;
    private double superTrendValue;

    @Override
    public String toString() {
        return String.join(",", tsCode, tradeDate, action,
                String.format("%.2f", closePrice),
                String.format("%.2f", superTrendValue));
    }

    private static final String TO_STRING_FORMAT = "股票代码: {%s}, 信号产生日期: {%s}, 超级趋势信号: {%s}";

    public SuperTrendTradeSignal(String tsCode, String tradeDate, String action) {
        this.tsCode = tsCode;
        this.tradeDate = tradeDate;
        this.action = action;
    }
}
