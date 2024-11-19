package com.lv.score.ScoreModel.calculate.save;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultMonth;
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
@Document(indexName = "month_score")
public class CalculateResultMonthEsEntity {
    @Id
    private String ts_code;
    @Field(type= FieldType.Text, name = "stock_name")
    private String stock_name;
    @Field(type= FieldType.Text, name = "tradeDateMonth")
    private String tradeDateMonth;
    // 指数代码
    @Field(type= FieldType.Text, name = "index_code")
    private String index_code;
    // 指数名称
    @Field(type= FieldType.Text, name = "index_code_name")
    private String index_code_name;

    @Field(type= FieldType.Double, name = "score")
    private Double score;


    public CalculateResultMonthEsEntity(CalculateResultMonth calculateResultMonth) {
        this.ts_code = calculateResultMonth.getTs_code();
        this.tradeDateMonth = calculateResultMonth.getTradeDateMonth();
        this.stock_name = calculateResultMonth.getStock_name();
        this.score = calculateResultMonth.getScore();
        this.index_code = calculateResultMonth.getIndex_code();
        this.index_code_name = calculateResultMonth.getIndex_code_name();
    }
}
