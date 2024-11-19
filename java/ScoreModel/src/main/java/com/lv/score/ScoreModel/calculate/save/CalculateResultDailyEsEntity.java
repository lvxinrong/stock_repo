package com.lv.score.ScoreModel.calculate.save;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultDaily;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
@Document(indexName = "daily_score")
public class CalculateResultDailyEsEntity {

    @Id
    private String id;
    @Field(type= FieldType.Text, name = "ts_code")
    private String ts_code;
    @Field(type= FieldType.Text, name = "index_ts_code")
    private String index_ts_code;
    @Field(type= FieldType.Text, name = "tradeDate")
    private String tradeDate;
    @Field(type= FieldType.Double, name = "score")
    private Double score;
    @Field(type= FieldType.Double, name = "stock_pct_chg")
    private Double stock_pct_chg;
    @Field(type= FieldType.Double, name = "index_pct_chg")
    private Double index_pct_chg;

    public CalculateResultDailyEsEntity(CalculateResultDaily calculateResultDaily) {
        ts_code = calculateResultDaily.getTs_code();
        index_ts_code = calculateResultDaily.getIndex_ts_code();
        tradeDate = calculateResultDaily.getTradeDate();
        score = calculateResultDaily.getScore();
        stock_pct_chg = calculateResultDaily.getStock_pct_chg();
        index_pct_chg = calculateResultDaily.getIndex_pct_chg();
        id = calculateResultDaily.getTs_code() + " " + calculateResultDaily.getTradeDate();
    }
}
