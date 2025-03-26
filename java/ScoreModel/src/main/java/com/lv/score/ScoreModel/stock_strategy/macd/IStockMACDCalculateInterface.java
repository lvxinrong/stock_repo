package com.lv.score.ScoreModel.stock_strategy.macd;

import com.lv.score.ScoreModel.stock_strategy.macd.entity.MACD20DayAnalysisResult;
import com.lv.score.ScoreModel.stock_strategy.macd.entity.MACD5DayAnalysisResult;

import java.util.Map;

public interface IStockMACDCalculateInterface {

    Map<String, MACD5DayAnalysisResult> getLatest5DaysData();


    Map<String, MACD20DayAnalysisResult> getLatest20DaysData();
}
