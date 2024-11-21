package com.lv.score.ScoreModel.cal_core.yield_rate;

/**
 * 计算某个股票的收益率
 */
public interface CalculateStockYieldRate {

    Double getStockYieldRateWithStartTime(String tsCode, String startTime, String endTime);
}
