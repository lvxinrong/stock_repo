package com.lv.score.ScoreModel.calculate;

import com.lv.score.ScoreModel.calculate.entity.CalculateResultDaily;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultMonth;
import com.lv.score.ScoreModel.calculate.entity.StockScoreInputParam;

import java.util.List;

public interface CalculateStockScore {

    List<CalculateResultDaily> getStockScore(StockScoreInputParam stockScoreInputParam);

    CalculateResultMonth getStockScoreMonth(List<CalculateResultDaily> calculateResultDailyList);
}
