package com.lv.score.ScoreModel.stock_strategy.stock_cyq.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
@Document(indexName = "stock_cyq_20")
public class StockCyqConcentratedEsDTO {

    @Id
    private String id;
    @Field(type= FieldType.Text, name = "tsCode")
    private String tsCode;
    // 计算期间，最新的交易日期
    @Field(type= FieldType.Text, name = "lastTradeDate")
    private String lastTradeDate;
    // 当前价格
    @Field(type= FieldType.Double, name = "currentPrice")
    private BigDecimal currentPrice;
    // 指标计算
    @Field(type= FieldType.Nested, name = "convergeRatio")
    private List<BigDecimal> convergeRatio;
    @Field(type= FieldType.Double, name = "lastConvergeRatio")
    private BigDecimal lastConvergeRatio;
    // 收敛趋势验证
    @Field(type= FieldType.Boolean, name = "lastConvergeRatio")
    private Boolean convergeValid;
    // 主力行为验证
    @Field(type= FieldType.Boolean, name = "lastConvergeRatio")
    private Boolean controlValid;
    //4. 量价验证
    @Field(type= FieldType.Boolean, name = "lastConvergeRatio")
    private Boolean priceValid;
    @Field(type= FieldType.Boolean, name = "lastConvergeRatio")
    private Boolean isGood;

    public StockCyqConcentratedEsDTO(StockCyqConcentratedResult data) {
        this.id = data.getTsCode() + "_" + data.getLastTradeDate();
        this.tsCode = data.getTsCode();
        this.lastTradeDate = data.getLastTradeDate();
        this.currentPrice = data.getCurrentPrice();
        this.convergeRatio = new ArrayList<>(data.getConvergeRatio());
        this.lastConvergeRatio = data.getLastConvergeRatio();
        this.convergeValid = data.isConvergeValid();
        this.controlValid = data.isControlValid();
        this.priceValid = data.isPriceValid();
        this.isGood = data.isGood();
    }
}
