package com.lv.score.ScoreModel.stock_repo;

import com.lv.score.ScoreModel.calculate.entity.CalculateResultDaily;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultMonth;
import com.lv.score.ScoreModel.calculate.entity.StockScoreInputParam;
import com.lv.score.ScoreModel.entity.Hs300Stock;

import java.util.List;
import java.util.Map;

public interface CalculateHs300Score {

    /**
     * String ts_code
     * List<CalculateResultDaily> 上个月每天交易分数
     */
    Map<String, List<CalculateResultDaily>> getStockScore();

    CalculateResultMonth getStockScoreMonth(List<CalculateResultDaily> calculateResultDailyList);
}
