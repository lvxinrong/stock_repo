package com.lv.score.ScoreModel.calculate.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StockScoreInputParam {

    private String ts_code;
    //yyyyMM
    private String date;
}
