package com.lv.score.ScoreModel.calCore.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class IndexStock {

    /**
     * 指数代码
     */
    private String indexCode;

    /**
     * 成分代码
     */
    private String conCode;

    /**
     * 交易日期
     */
    private String tradeDate;

    /**
     * 权重
     */
    private Double weight;
}
