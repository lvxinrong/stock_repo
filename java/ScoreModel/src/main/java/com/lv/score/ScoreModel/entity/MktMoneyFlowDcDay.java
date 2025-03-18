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
 * 大盘资金流向（DC）
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("mkt_money_flow_dc_day")
public class MktMoneyFlowDcDay implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 交易日期
     */
    private String tradeDate;

    /**
     * 上证收盘价(点)
     */
    private BigDecimal closeSh;

    /**
     * 上证涨跌幅(%)
     */
    private BigDecimal pctChangeSh;

    /**
     * 深证收盘价(点)
     */
    private BigDecimal closeSz;

    /**
     * 深证涨跌幅(%)
     */
    private BigDecimal pctChangeSz;

    /**
     * 主力净流入净额(元)
     */
    private BigDecimal netAmount;

    /**
     * 主力净流入净占比(%)
     */
    private BigDecimal netAmountRate;

    /**
     * 超大单净流入净额(元)
     */
    private BigDecimal buyElgAmount;

    /**
     * 超大单净流入净占比(%)
     */
    private BigDecimal buyElgAmountRate;

    /**
     * 大单净流入净额(元)
     */
    private BigDecimal buyLgAmount;

    /**
     * 大单净流入净占比(%)
     */
    private BigDecimal buyLgAmountRate;

    /**
     * 中单净流入净额(元)
     */
    private BigDecimal buyMdAmount;

    /**
     * 中单净流入净占比(%)
     */
    private BigDecimal buyMdAmountRate;

    /**
     * 小单净流入净额(元)
     */
    private BigDecimal buySmAmount;

    /**
     * 小单净流入净占比(%)
     */
    private BigDecimal buySmAmountRate;
}
