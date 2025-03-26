package com.lv.score.ScoreModel.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 股票技术因子
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-18
 */
@Getter
@Setter
@TableName("stock_stk_factor_data")
public class StockStkFactorData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String tsCode;

    private String tradeDate;

    /**
     * 收盘价
     */
    private BigDecimal close;

    /**
     * 开盘价
     */
    private BigDecimal open;

    /**
     * 最高价
     */
    private BigDecimal high;

    /**
     * 最低价
     */
    private BigDecimal low;

    /**
     * 昨收价
     */
    private BigDecimal preClose;

    /**
     * 涨跌额
     */
    private BigDecimal changeValue;

    /**
     * 涨跌幅
     */
    private BigDecimal pctChange;

    /**
     * 成交量 （手）
     */
    private BigDecimal vol;

    /**
     * 成交额 （千元）
     */
    private BigDecimal amount;

    /**
     * 复权因子
     */
    private BigDecimal adjFactor;

    /**
     * 开盘价后复权
     */
    private BigDecimal openHfq;

    /**
     * 开盘价前复权
     */
    private BigDecimal openQfq;

    /**
     * 收盘价后复权
     */
    private BigDecimal closeHfq;

    /**
     * 收盘价前复权
     */
    private BigDecimal closeQfq;

    /**
     * 最高价后复权
     */
    private BigDecimal highHfq;

    /**
     * 最高价前复权
     */
    private BigDecimal highQfq;

    /**
     * 最低价后复权
     */
    private BigDecimal lowHfq;

    /**
     * 最低价前复权
     */
    private BigDecimal lowQfq;

    /**
     * 昨收价后复权
     */
    private BigDecimal preCloseHfq;

    /**
     * 昨收价前复权
     */
    private BigDecimal preCloseQfq;

    /**
     * MCAD_DIF (基于前复权价格计算，下同)
     */
    private BigDecimal macdDif;

    /**
     * MCAD_DEA
     */
    private BigDecimal macdDea;

    /**
     * MCAD
     */
    private BigDecimal macd;

    /**
     * KDJ_K
     */
    private BigDecimal kdjK;

    /**
     * KDJ_D
     */
    private BigDecimal kdjD;

    /**
     * KDJ_J
     */
    private BigDecimal kdjJ;

    /**
     * RSI_6
     */
    @TableField("RSI_6")
    private BigDecimal rsi6;

    /**
     * RSI_12
     */
    @TableField("RSI_12")
    private BigDecimal rsi12;

    /**
     * RSI_24
     */
    @TableField("RSI_24")
    private BigDecimal rsi24;

    /**
     * BOLL_UPPER
     */
    private BigDecimal bollUpper;

    /**
     * BOLL_MID
     */
    private BigDecimal bollMid;

    /**
     * BOLL_LOWER
     */
    private BigDecimal bollLower;

    /**
     * CCI
     */
    private BigDecimal cci;
}
