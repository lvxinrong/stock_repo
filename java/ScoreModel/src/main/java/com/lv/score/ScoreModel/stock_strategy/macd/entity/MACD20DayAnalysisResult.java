package com.lv.score.ScoreModel.stock_strategy.macd.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * 模块	实现要点	策略来源
 * 趋势判断	采用零轴上方的DIF值过滤多头市场，规避空头风险
 * 均线系统	要求5日均线>20日均线且现价>MA5，形成多头排列
 * 动量验证	连续3日MACD柱状图扩大，确保动能持续性
 * 量价共振	金叉日成交量突破20日均量1.2倍，排除虚假信号
 * 仓位管理	根据趋势强度动态调整仓位，每5%强度增加10%头寸
 * 风险控制	强制要求MACD位于零轴上方，避免逆势操作
 */

@Data
public class MACD20DayAnalysisResult {

    private String trendDirection;       // 趋势方向 UP/DOWN
    private BigDecimal trendStrength;    // 趋势强度百分比
    private String maAlignment;          // 均线排列 BULLISH/BEARISH
    private Boolean momentumValid;       // 动量是否有效
    private BigDecimal positiveMacdArea; // MACD正柱面积
    private Boolean volumeBreak = Boolean.FALSE;         // 成交量突破
    private Boolean multiFilterPassed;   // 多指标过滤
    private String buyFromatString;
    private Boolean buySignal;
    private String tsCode;
    private String lastTradeDate;
    private Integer buySignalTrueCount;
}
