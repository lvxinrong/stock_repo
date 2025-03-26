package com.lv.score.ScoreModel.stock_strategy.stock_cyq.entity;

import com.lv.score.ScoreModel.entity.DailyBasicInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class StockCyqConcentratedResult {

    private String tsCode;
    // 计算期间，最新的交易日期
    private String lastTradeDate;
    // 当前价格
    private BigDecimal currentPrice;
    // 指标计算
    private List<BigDecimal> convergeRatio;

    private BigDecimal lastConvergeRatio;
    // 收敛趋势验证
    private boolean convergeValid;
    // 主力行为验证
    private boolean controlValid;
    //4. 量价验证
    private boolean priceValid;

    private boolean isGood;

    private DailyBasicInfo LastDailyBasicInfo;


}
