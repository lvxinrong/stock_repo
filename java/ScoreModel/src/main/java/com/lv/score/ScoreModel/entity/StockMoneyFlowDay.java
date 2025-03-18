package com.lv.score.ScoreModel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 个股资金流向表
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("stock_money_flow_day")
public class StockMoneyFlowDay implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 交易日期
     */
    private String tradeDate;

    /**
     * TS代码
     */
    private String tsCode;

    /**
     * 小单买入量（手）
     */
    private Integer buySmVol;

    /**
     * 小单买入金额（万元）
     */
    private BigDecimal buySmAmount;

    /**
     * 小单卖出量（手）
     */
    private Integer sellSmVol;

    /**
     * 小单卖出金额（万元）
     */
    private BigDecimal sellSmAmount;

    /**
     * 中单买入量（手）
     */
    private Integer buyMdVol;

    /**
     * 中单买入金额（万元）
     */
    private BigDecimal buyMdAmount;

    /**
     * 中单卖出量（手）
     */
    private Integer sellMdVol;

    /**
     * 中单卖出金额（万元）
     */
    private BigDecimal sellMdAmount;

    /**
     * 大单买入量（手）
     */
    private Integer buyLgVol;

    /**
     * 大单买入金额（万元）
     */
    private BigDecimal buyLgAmount;

    /**
     * 大单卖出量（手）
     */
    private Integer sellLgVol;

    /**
     * 大单卖出金额（万元）
     */
    private BigDecimal sellLgAmount;

    /**
     * 特大单买入量（手）
     */
    private Integer buyElgVol;

    /**
     * 特大单买入金额（万元）
     */
    private BigDecimal buyElgAmount;

    /**
     * 特大单卖出量（手）
     */
    private Integer sellElgVol;

    /**
     * 特大单卖出金额（万元）
     */
    private BigDecimal sellElgAmount;

    /**
     * 净流入量（手）
     */
    private Integer netMfVol;

    /**
     * 净流入额（万元）
     */
    private BigDecimal netMfAmount;

    /**
     * 换手率(%)
     */
    private BigDecimal turnoverRate;
}
