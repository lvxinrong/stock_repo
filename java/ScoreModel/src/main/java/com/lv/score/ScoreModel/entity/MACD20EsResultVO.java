package com.lv.score.ScoreModel.entity;

import com.lv.score.ScoreModel.stock_strategy.macd.entity.MACD20EsResult;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MACD20EsResultVO {
    private String id;
    private String tsCode;
    private String stockName;
    private String lastTradeDate;
    private String trendDirection;       // 趋势方向 UP/DOWN
    private BigDecimal trendStrength;    // 趋势强度百分比
    private String maAlignment;          // 均线排列 BULLISH/BEARISH
    private boolean momentumValid;       // 动量是否有效
    private BigDecimal positiveMacdArea; // MACD正柱面积
    private boolean volumeBreak;         // 成交量突破
    private boolean multiFilterPassed;   // 多指标过滤
    private String buyFormatString; // 购买建议
    private boolean buySignal;
    // 累计涨幅
    private BigDecimal cumulativeIncrease;
    // 累计推荐买入次数
    private int buySignalTrueCount;
    // 总市值
    private double totalVm;

    public MACD20EsResultVO(MACD20EsResult dto) {
        this.id = dto.getId();
        this.tsCode = dto.getTsCode();
        this.lastTradeDate = dto.getLastTradeDate();
        this.trendDirection = dto.getTrendDirection();
        this.trendStrength = dto.getTrendStrength();
        this.maAlignment = dto.getMaAlignment();
        this.momentumValid = dto.isMomentumValid();
        this.positiveMacdArea = dto.getPositiveMacdArea();
        this.volumeBreak = dto.isVolumeBreak();
        this.multiFilterPassed = dto.isMultiFilterPassed();
        this.buyFormatString = dto.getBuyFormatString();
        this.buySignal = dto.isBuySignal();
        this.buySignalTrueCount = dto.getBuySignalTrueCount() == null ? 0 : dto.getBuySignalTrueCount();
    }
}
