package com.lv.score.ScoreModel;

import com.lv.score.ScoreModel.entity.StockStkFactorData;
import com.lv.score.ScoreModel.service.IStockStkFactorDataService;
import com.lv.score.ScoreModel.stock_strategy.macd.analyzers.MACD20DayAnalyzer;
import com.lv.score.ScoreModel.stock_strategy.macd.analyzers.MACD5DayAnalyzer;
import com.lv.score.ScoreModel.stock_strategy.macd.entity.MACD20DayAnalysisResult;
import com.lv.score.ScoreModel.stock_strategy.macd.entity.MACD5DayAnalysisResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
public class MACD5DayAnalyzerTest {

    @Autowired
    IStockStkFactorDataService iStockStkFactorDataService;

    @Test
    public void testAnalyzeLast5Days() {
        List<StockStkFactorData> data = iStockStkFactorDataService.getLast5DaysData();
        Map<String, List<StockStkFactorData>> map = data.stream().collect(Collectors.groupingBy(StockStkFactorData::getTsCode));
        Map<String, MACD5DayAnalysisResult> result = new HashMap<>();
        for (Map.Entry<String, List<StockStkFactorData>> entry : map.entrySet()) {
            try {
                String tsCode = entry.getKey();
                MACD5DayAnalysisResult macd5DayAnalysisResult = MACD5DayAnalyzer.analyzeLast5Days(entry.getValue());
                result.put(tsCode, macd5DayAnalysisResult);
            } catch (Exception e) {
                System.out.println("cal exception. tsCode:" + entry.getKey());
            }
        }
        System.out.println(result);
    }

    @Test
    public void testAnalyzeLast20Days() {
        List<StockStkFactorData> data = iStockStkFactorDataService.getLast20DaysData();
        Map<String, List<StockStkFactorData>> map = data.stream().collect(Collectors.groupingBy(StockStkFactorData::getTsCode));
        Map<String, MACD20DayAnalysisResult> result = new HashMap<>();
        for (Map.Entry<String, List<StockStkFactorData>> entry : map.entrySet()) {
            try {
                String tsCode = entry.getKey();
                MACD20DayAnalysisResult macd20DayAnalysisResult = MACD20DayAnalyzer.analyze20Day(entry.getValue());
                result.put(tsCode, macd20DayAnalysisResult);
            } catch (Exception e) {
                System.out.println("cal exception. tsCode:" + entry.getKey());
                e.printStackTrace();

            }
        }
        System.out.println(result);
    }
}
