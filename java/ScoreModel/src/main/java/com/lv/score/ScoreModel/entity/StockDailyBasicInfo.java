package com.lv.score.ScoreModel.entity;

import lombok.Data;

@Data
public class StockDailyBasicInfo {

    private String tsCode;


    private String name;

    /**
     * 当日收盘价
     */
    private Double close;

    /**
     * 总市值（万元）
     */
    private Double totalMv;

    /**
     * 流通市值（万元）
     */
    private Double circMv;

    /**
     * 上市日期
     */
    private String listDate;

    /**
     * 市盈率（总市值/净利润，亏损的PE为空）
     */
    private Double pe;
}
