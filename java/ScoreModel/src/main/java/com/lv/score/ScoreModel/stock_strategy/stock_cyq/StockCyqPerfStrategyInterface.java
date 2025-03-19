package com.lv.score.ScoreModel.stock_strategy.stock_cyq;

import com.lv.score.ScoreModel.entity.StockBasic;

import java.util.List;

/**
 * 根据股票的每日筹码结构选取比较好的股票
 */
public interface StockCyqPerfStrategyInterface {

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

}
