package com.lv.score.ScoreModel.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2024-11-15
 */
@TableName("index_basic_daily")
public class IndexBasicDaily implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 股票代码
     */
    private String tsCode;

    /**
     * 交易日期
     */
    private String tradeDate;

    /**
     * 开盘价
     */
    private Double open;

    /**
     * 最高价
     */
    private Double high;

    /**
     * 	最低价
     */
    private Double low;

    /**
     * 收盘价
     */
    private Double close;

    /**
     * 昨收价除权价，前复权
     */
    private Double preClose;

    /**
     * 涨跌额，tushare原字段 change
     */
    private Double changeValue;

    /**
     * 涨跌幅 基于除权后的昨收计算的涨跌幅：（今收-除权昨收）/除权昨收 
     */
    private Double pctChg;

    /**
     * 成交量 单位 手
     */
    private Double vol;

    /**
     * 成交额 单位 千元
     */
    private Double amount;

    public String getTsCode() {
        return tsCode;
    }

    public void setTsCode(String tsCode) {
        this.tsCode = tsCode;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getPreClose() {
        return preClose;
    }

    public void setPreClose(Double preClose) {
        this.preClose = preClose;
    }

    public Double getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(Double changeValue) {
        this.changeValue = changeValue;
    }

    public Double getPctChg() {
        return pctChg;
    }

    public void setPctChg(Double pctChg) {
        this.pctChg = pctChg;
    }

    public Double getVol() {
        return vol;
    }

    public void setVol(Double vol) {
        this.vol = vol;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "IndexBasicDaily{" +
        "tsCode = " + tsCode +
        ", tradeDate = " + tradeDate +
        ", open = " + open +
        ", high = " + high +
        ", low = " + low +
        ", close = " + close +
        ", preClose = " + preClose +
        ", changeValue = " + changeValue +
        ", pctChg = " + pctChg +
        ", vol = " + vol +
        ", amount = " + amount +
        "}";
    }
}
