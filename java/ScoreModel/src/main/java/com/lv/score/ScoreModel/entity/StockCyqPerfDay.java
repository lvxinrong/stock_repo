package com.lv.score.ScoreModel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 每日筹码及胜率
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-18
 */
@Getter
@Setter
@TableName("stock_cyq_perf_day")
public class StockCyqPerfDay implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 股票代码
     */
    private String tsCode;

    /**
     * 交易日期
     */
    private String tradeDate;

    /**
     * 历史最低价
     */
    private BigDecimal hisLow;

    /**
     * 历史最高价
     */
    private BigDecimal hisHigh;

    /**
     * 5分位成本
     */
    @TableField("cost_5pct")
    private BigDecimal cost5pct;

    /**
     * 15分位成本
     */
    @TableField("cost_15pct")
    private BigDecimal cost15pct;

    /**
     * 50分位成本
     */
    @TableField("cost_50pct")
    private BigDecimal cost50pct;

    /**
     * 85分位成本
     */
    @TableField("cost_85pct")
    private BigDecimal cost85pct;

    /**
     * 95分位成本
     */
    @TableField("cost_95pct")
    private BigDecimal cost95pct;

    /**
     * 加权平均成本
     */
    private BigDecimal weightAvg;

    /**
     * 胜率
     */
    private BigDecimal winnerRate;
}
