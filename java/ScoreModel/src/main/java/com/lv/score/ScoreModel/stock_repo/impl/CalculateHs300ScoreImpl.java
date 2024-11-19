package com.lv.score.ScoreModel.stock_repo.impl;

import com.lv.score.ScoreModel.calculate.CalculateStockScore;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultDaily;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultMonth;
import com.lv.score.ScoreModel.calculate.entity.StockScoreInputParam;
import com.lv.score.ScoreModel.entity.Hs300Stock;
import com.lv.score.ScoreModel.entity.IndexBasicDaily;
import com.lv.score.ScoreModel.service.IIndexBasicDailyService;
import com.lv.score.ScoreModel.stock_repo.CalculateHs300Score;
import com.lv.score.ScoreModel.stock_repo.HS_300;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CalculateHs300ScoreImpl implements CalculateHs300Score {

    @Autowired
    HS_300 hs300;

    @Autowired
    CalculateStockScore calculateStockScore;

    @Autowired
    IIndexBasicDailyService iIndexBasicDailyService;

    @Value("${stock.stock_month}")
    private String stock_month;

    @Override
    public Map<String, List<CalculateResultDaily>> getStockScore() {
        Map<String, List<CalculateResultDaily>> result = new HashMap<>();
        List<IndexBasicDaily> indexBasicDailies = iIndexBasicDailyService.getTradeDailyDataByTsCodeAndTradeDate("399300.SZ", stock_month);
        List<Hs300Stock> hs300Stocks = hs300.getHs300StockList(null);
        for (Hs300Stock stock : hs300Stocks) {
            StockScoreInputParam stockScoreInputParam = new StockScoreInputParam();
            stockScoreInputParam.setTs_code(stock.getConCode());
            stockScoreInputParam.setDate(stock_month);
            stockScoreInputParam.setIndexBasicDailies(indexBasicDailies);
            List<CalculateResultDaily> calculateResultDailyList = calculateStockScore.getStockScoreByIndex(stockScoreInputParam);
            result.put(stock.getConCode(), calculateResultDailyList);
        }
        return result;
    }

    @Override
    public CalculateResultMonth getStockScoreMonth(List<CalculateResultDaily> calculateResultDailyList) {
        return null;
    }
}
