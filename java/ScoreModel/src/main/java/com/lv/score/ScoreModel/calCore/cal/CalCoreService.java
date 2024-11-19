package com.lv.score.ScoreModel.calCore.cal;

import com.lv.score.ScoreModel.calculate.entity.CalculateResultDaily;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultMonth;
import com.lv.score.ScoreModel.entity.IndexBasicDaily;
import com.lv.score.ScoreModel.entity.TradeDaily;

import java.util.List;
import java.util.Map;

public interface CalCoreService {

    /**
     * 计算单个股票和指数的分数
     * @param indexBasicDaily
     * @return
     */
    CalculateResultDaily calStockScoreWithIndex(IndexBasicDaily indexBasicDaily, TradeDaily tradeDaily);

    /**
     * 计算单个股票和指数的分数
     */
    List<CalculateResultDaily> calStockScoreList(Map<String, IndexBasicDaily> indexBasicDailyMap, List<TradeDaily> tradeDailyList);

    CalculateResultMonth getMonthScore(List<CalculateResultDaily> calculateResultDailyList);
}
