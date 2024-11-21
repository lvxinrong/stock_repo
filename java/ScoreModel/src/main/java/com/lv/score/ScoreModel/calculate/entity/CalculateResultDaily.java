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

    /**
     * 开盘价
     */
    private Double open;

    /**
     * 最高价
     */
    private Double high;

    /**
     * 	最低价
     */
    private Double low;

    /**
     * 收盘价
     */
    private Double close;

    /**
     * 昨收价除权价，前复权
     */
    private Double preClose;
}
