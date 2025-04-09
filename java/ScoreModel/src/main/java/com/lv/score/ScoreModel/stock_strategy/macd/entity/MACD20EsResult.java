package com.lv.score.ScoreModel.stock_strategy.macd.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
@Document(indexName = "macd_20")
public class MACD20EsResult {

    @Id
    private String id;
    @Field(type= FieldType.Text, name = "tsCode")
    private String tsCode;
    @Field(type= FieldType.Text, name = "lastTradeDate")
    private String lastTradeDate;
    @Field(type= FieldType.Text, name = "trendDirection")
    private String trendDirection;       // 趋势方向 UP/DOWN
    @Field(type= FieldType.Double, name = "trendStrength")
    private BigDecimal trendStrength;    // 趋势强度百分比
    @Field(type= FieldType.Text, name = "maAlignment")
    private String maAlignment;          // 均线排列 BULLISH/BEARISH
    @Field(type= FieldType.Boolean, name = "momentumValid")
    private boolean momentumValid;       // 动量是否有效
    @Field(type= FieldType.Double, name = "positiveMacdArea")
    private BigDecimal positiveMacdArea; // MACD正柱面积
    @Field(type= FieldType.Boolean, name = "volumeBreak")
    private boolean volumeBreak;         // 成交量突破
    @Field(type= FieldType.Boolean, name = "multiFilterPassed")
    private boolean multiFilterPassed;   // 多指标过滤
    @Field(type= FieldType.Text, name = "buyFormatString")
    private String buyFormatString; // 购买建议
    @Field(type= FieldType.Boolean, name = "buySignal")
    private boolean buySignal;
    @Field(type = FieldType.Integer, name = "buySignalTrueCount")
    private Integer buySignalTrueCount;


    public MACD20EsResult(MACD20DayAnalysisResult data) {
        this.id = data.getTsCode() + "_" + data.getLastTradeDate();
        this.tsCode = data.getTsCode();
        this.lastTradeDate = data.getLastTradeDate();
        this.trendDirection = data.getTrendDirection();
        this.trendStrength = data.getTrendStrength();
        this.maAlignment = data.getMaAlignment();
        this.momentumValid = data.getMomentumValid() != null && data.getMomentumValid();
        this.positiveMacdArea = data.getPositiveMacdArea();
        this.volumeBreak = data.getVolumeBreak() != null && data.getVolumeBreak();
        this.multiFilterPassed = data.getMultiFilterPassed() != null && data.getMultiFilterPassed();
        this.buyFormatString = data.getBuyFromatString();
        this.buySignal = data.getBuySignal();
        this.buySignalTrueCount = data.getBuySignalTrueCount();
    }



}
