package com.lv.score.ScoreModel.calculate;

import com.lv.score.ScoreModel.calculate.entity.CalculateResultDaily;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultMonth;
import com.lv.score.ScoreModel.calculate.entity.StockScoreInputParam;

import java.util.List;

public interface CalculateStockScore {


    /**
     * 通过ts_code 判断所属板块，主要区分创业板，主板，用于计算和主板的分数
     * @param stockScoreInputParam
     * @return
     */
    List<CalculateResultDaily> getStockScore(StockScoreInputParam stockScoreInputParam);

    /**
     * 用于计算和指数的偏差分数 index_code
     * @param stockScoreInputParam
     * @return
     */
    List<CalculateResultDaily> getStockScoreByIndex(StockScoreInputParam stockScoreInputParam);

    CalculateResultMonth getStockScoreMonth(List<CalculateResultDaily> calculateResultDailyList);
}
