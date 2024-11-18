package com.lv.score.ScoreModel.calculate.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CalculateResultDaily implements Serializable {

    private String ts_code;

    private String index_ts_code;

    private String tradeDate;

    private Double score;

    private Double stock_pct_chg;

    private Double index_pct_chg;
}
