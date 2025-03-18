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
 * 板块资金流向(同花顺)
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("ind_money_flow_ths_day")
public class IndMoneyFlowThsDay implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 交易日期
     */
    private String tradeDate;

    /**
     * 板块代码
     */
    private String tsCode;

    /**
     * 板块名称
     */
    private String industry;

    /**
     * 领涨股票名称
     */
    private String leadStock;

    /**
     * 收盘指数
     */
    private BigDecimal close;

    /**
     * 指数涨跌幅(%)
     */
    private BigDecimal pctChange;

    /**
     * 公司数量
     */
    private Integer companyNum;

    /**
     * 领涨股涨跌幅(%)
     */
    private BigDecimal pctChangeStock;

    /**
     * 领涨股最新价
     */
    private BigDecimal closePrice;

    /**
     * 流入资金(亿元)
     */
    private BigDecimal netBuyAmount;

    /**
     * 流出资金(亿元)
     */
    private BigDecimal netSellAmount;

    /**
     * 净额(元)
     */
    private BigDecimal netAmount;
}
