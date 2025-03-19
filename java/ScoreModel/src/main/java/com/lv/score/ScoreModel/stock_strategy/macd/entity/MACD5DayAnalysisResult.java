package com.lv.score.ScoreModel.stock_strategy.macd.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class MACD5DayAnalysisResult {

    private List<String> goldenCrossDays = new ArrayList<>();  // 出现金叉的日期
    private List<String> deadCrossDays = new ArrayList<>();    // 出现死叉的日期
    private boolean doubleRed;             // 是否二次翻红
    private BigDecimal avgMacdHistogram;   // 5日平均柱状值
    private long daysAboveZero;            // DIF在零轴上方的天数
    private boolean topDivergence;         // 顶背离信号
    private boolean bottomDivergence;      // 底背离信号
}
