package com.lv.score.ScoreModel.stock_strategy.stock_cyq;

import com.lv.score.ScoreModel.stock_strategy.stock_cyq.entity.StockCyqConcentratedResult;

import java.util.List;

/**
 * 根据股票的每日筹码结构选取比较好的股票
 */
public interface IStockCyqPerfStrategyInterface {

    /**
     * 返回过去5个交易日筹码结构比较好的股票
     * @return
     */
    List<String> getGoodStockCyqPerfLatest5Days();

    /**
     * 返回过去20个交易日筹码结构比较好的股票
     * @return
     */
    List<String> getGoodStockCyqPerfLatest20Days();

    /**
     * 计算过去20个交易日，筹码结构逐步集中的股票
     */
    List<StockCyqConcentratedResult> getLast20DaysStock();

}
