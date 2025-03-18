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
 * 板块资金流向（DC）
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("ind_money_flow_dc_day")
public class IndMoneyFlowDcDay implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 交易日期
     */
    private String tradeDate;

    /**
     * 数据类型
     */
    private String contentType;

    /**
     * DC板块代码
     */
    private String tsCode;

    /**
     * 板块名称
     */
    private String name;

    /**
     * 板块涨跌幅(%)
     */
    private BigDecimal pctChange;

    /**
     * 板块最新指数
     */
    private BigDecimal close;

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

    /**
     * 主力净流入最大股
     */
    private String buySmAmountStock;

    /**
     * 序号
     */
    private Integer rankValue;
}
