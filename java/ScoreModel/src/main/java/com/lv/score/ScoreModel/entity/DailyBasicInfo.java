package com.lv.score.ScoreModel.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 股票交易及财务数据
 * </p>
 *
 * @author lvxinrong
 * @since 2024-11-19
 */
@Getter
@Setter
@TableName("daily_basic_info")
public class DailyBasicInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * TS股票代码
     */
    private String tsCode;

    /**
     * 交易日期
     */
    private String tradeDate;

    /**
     * 当日收盘价
     */
    private Double close;

    /**
     * 换手率（%）
     */
    private Double turnoverRate;

    /**
     * 换手率（自由流通股）
     */
    private Double turnoverRateF;

    /**
     * 量比
     */
    private Double volumeRatio;

    /**
     * 市盈率（总市值/净利润，亏损的PE为空）
     */
    private Double pe;

    /**
     * 市盈率（TTM，亏损的PE为空）
     */
    private Double peTtm;

    /**
     * 市净率（总市值/净资产）
     */
    private Double pb;

    /**
     * 市销率
     */
    private Double ps;

    /**
     * 市销率（TTM）
     */
    private Double psTtm;

    /**
     * 股息率（%）
     */
    private Double dvRatio;

    /**
     * 股息率（TTM）（%）
     */
    private Double dvTtm;

    /**
     * 总股本（万股）
     */
    private Double totalShare;

    /**
     * 流通股本（万股）
     */
    private Double floatShare;

    /**
     * 自由流通股本（万）
     */
    private Double freeShare;

    /**
     * 总市值（万元）
     */
    private Double totalMv;

    /**
     * 流通市值（万元）
     */
    private Double circMv;
}
