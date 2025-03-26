package com.lv.score.ScoreModel.stock_strategy.kdj.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "kdj_14")
public class KDJDayAnalysisEsResultEntity {

    @Id
    private String id;
    @Field(type= FieldType.Text, name = "tsCode")
    private String tsCode;
    @Field(type= FieldType.Text, name = "tradeDate")
    private String tradeDate;
    @Field(type= FieldType.Double, name = "rsv")
    private double rsv;
    @Field(type= FieldType.Double, name = "k")
    private double k;
    @Field(type= FieldType.Double, name = "d")
    private double d;
    @Field(type= FieldType.Double, name = "j")
    private double j;
    @Field(type= FieldType.Boolean, name = "overbought")
    private boolean overbought;  // J值超买（>80）
    @Field(type= FieldType.Boolean, name = "oversold")
    private boolean oversold;   // J值超卖（<20）
    @Field(type= FieldType.Boolean, name = "goldenCross")
    private boolean goldenCross;// K线上穿D线
    @Field(type= FieldType.Boolean, name = "deadCross")
    private boolean deadCross;  // K线下穿D线

    public KDJDayAnalysisEsResultEntity(KDJDayAnalysisResult data) {
        this.id = data.getTsCode() + "_" + data.getTradeDate();
        this.tsCode = data.getTsCode();
        this.tradeDate = data.getTradeDate();
        this.k = data.getK();
        this.j = data.getJ();
        this.d = data.getD();
        this.overbought = data.isOverbought();
        this.oversold = data.isOversold();
        this.goldenCross = data.isGoldenCross();
        this.deadCross = data.isDeadCross();
    }
}
