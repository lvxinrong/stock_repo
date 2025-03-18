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
 * 个股资金流向明细表
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("stock_money_flow_ths_day")
public class StockMoneyFlowThsDay implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 交易日期
     */
    private String tradeDate;

    /**
     * 股票代码
     */
    private String tsCode;

    /**
     * 股票名称
     */
    private String name;

    /**
     * 涨跌幅(%)
     */
    private BigDecimal pctChange;

    /**
     * 最新价
     */
    private BigDecimal latest;

    /**
     * 资金净流入(万元)
     */
    private BigDecimal netAmount;

    /**
     * 5日主力净额(万元)
     */
    private BigDecimal netD5Amount;

    /**
     * 大单净流入额(万元)
     */
    private BigDecimal buyLgAmount;

    /**
     * 大单净流入占比(%)
     */
    private BigDecimal buyLgAmountRate;

    /**
     * 中单净流入额(万元)
     */
    private BigDecimal buyMdAmount;

    /**
     * 中单净流入占比(%)
     */
    private BigDecimal buyMdAmountRate;

    /**
     * 小单净流入额(万元)
     */
    private BigDecimal buySmAmount;

    /**
     * 小单净流入占比(%)
     */
    private BigDecimal buySmAmountRate;
}
