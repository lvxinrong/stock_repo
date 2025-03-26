package com.lv.score.ScoreModel.stock_strategy.kdj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KDJDayAnalysisResult {

    private String tsCode;
    private String tradeDate;
    private double rsv;
    private double k;
    private double d;
    private double j;
    private boolean overbought;  // J值超买（>80）
    private boolean oversold;   // J值超卖（<20）
    private boolean goldenCross;// K线上穿D线
    private boolean deadCross;  // K线下穿D线
}
