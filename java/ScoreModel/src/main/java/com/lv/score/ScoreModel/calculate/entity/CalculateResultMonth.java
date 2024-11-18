package com.lv.score.ScoreModel.calculate.entity;

import lombok.Data;

@Data
public class CalculateResultMonth {

    private String ts_code;

    private String stock_name;

    private String tradeDateMonth;

    private Double score;
}
