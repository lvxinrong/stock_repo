package com.lv.score.ScoreModel.stock_strategy.macd.impl;

import com.lv.score.ScoreModel.entity.StockStkFactorData;
import com.lv.score.ScoreModel.service.IStockStkFactorDataService;
import com.lv.score.ScoreModel.stock_strategy.macd.analyzers.MACD20DayAnalyzer;
import com.lv.score.ScoreModel.stock_strategy.macd.analyzers.MACD5DayAnalyzer;
import com.lv.score.ScoreModel.stock_strategy.macd.StockMACDCalculateInterface;
import com.lv.score.ScoreModel.stock_strategy.macd.entity.MACD20DayAnalysisResult;
import com.lv.score.ScoreModel.stock_strategy.macd.entity.MACD5DayAnalysisResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StockMACDCalculateInterfaceImpl implements StockMACDCalculateInterface {

    @Autowired
    IStockStkFactorDataService iStockStkFactorDataService;

    @Override
    public Map<String, MACD5DayAnalysisResult> getLatest5DaysData() {
        List<StockStkFactorData> data = iStockStkFactorDataService.getLast5DaysData();
        Map<String, List<StockStkFactorData>> map = data.stream().collect(Collectors.groupingBy(StockStkFactorData::getTsCode));
        Map<String, MACD5DayAnalysisResult> result = new HashMap<>();
        String lastDataMysql = this.iStockStkFactorDataService.getLatestDate();
        for (Map.Entry<String, List<StockStkFactorData>> entry : map.entrySet()) {
            try {
                if (entry.getValue().get(0).getTradeDate().equals(lastDataMysql)) {
                    String tsCode = entry.getKey();
                    MACD5DayAnalysisResult macd5DayAnalysisResult = MACD5DayAnalyzer.analyzeLast5Days(entry.getValue());
                    result.put(tsCode, macd5DayAnalysisResult);
                } else {
                    log.warn("股票: {}, 技术因子数据缺失，不是最新的数据。", entry.getKey());
                }
            } catch (Exception e) {
                log.error("calculate MACD error. tsCode:{}", entry.getKey(), e);
            }
        }
        return result;
    }

    @Override
    public Map<String, MACD20DayAnalysisResult> getLatest20DaysData() {
        List<StockStkFactorData> data = iStockStkFactorDataService.getLast20DaysData();
        Map<String, List<StockStkFactorData>> map = data.stream().collect(Collectors.groupingBy(StockStkFactorData::getTsCode));
        Map<String, MACD20DayAnalysisResult> result = new HashMap<>();
        String lastDataMysql = this.iStockStkFactorDataService.getLatestDate();
        for (Map.Entry<String, List<StockStkFactorData>> entry : map.entrySet()) {
            try {
                if (lastDataMysql.equals(entry.getValue().get(0).getTradeDate())) {
                    String tsCode = entry.getKey();
                    MACD20DayAnalysisResult macd20DayAnalysisResult = MACD20DayAnalyzer.analyze20Day(entry.getValue());
                    cal20DayResult(macd20DayAnalysisResult, entry.getValue());
                    result.put(tsCode, macd20DayAnalysisResult);
                } else {
                    log.warn("股票: {}, 技术因子数据缺失，不是最新的数据。", entry.getKey());
                }
            } catch (Exception e) {
                log.error("calculate MACD error. tsCode:{}", entry.getKey(), e);
            }
        }
        return result;
    }

    private void cal20DayResult(MACD20DayAnalysisResult result, List<StockStkFactorData> stockStkFactorDataList) {
        boolean buySignal = result.getTrendDirection().equals("UP")
                && result.getMaAlignment().equals("BULLISH")
                && result.getMomentumValid()
                && result.getVolumeBreak()
                && result.getTrendStrength().compareTo(new BigDecimal(5)) > 0
                && result.getMultiFilterPassed();
        result.setBuySignal(buySignal);
        StockStkFactorData last = stockStkFactorDataList.get(stockStkFactorDataList.size() - 1);
        if (buySignal) {
            String buyStringFormat = """
                    [交易信号] 符合买入条件：
                    标的: %s
                    价格: %s
                    建议仓位: %s
                    """;
            result.setBuyFromatString(String.format(buyStringFormat, last.getTsCode(), last.getClose(), calculatePositionSize(result)));
        } else {
            result.setBuyFromatString("[决策建议] 当前不符合买入条件");
        }
    }

    // 动态仓位计算（参考网页1的仓位管理）
    private static BigDecimal calculatePositionSize(MACD20DayAnalysisResult result) {
        BigDecimal base = new BigDecimal("100"); // 基础单位
        // 每5%趋势强度增加10%仓位（上限200%）
        BigDecimal ratio = result.getTrendStrength()
                .divide(new BigDecimal(5), 2, BigDecimal.ROUND_HALF_UP)
                .min(new BigDecimal(2));
        return base.multiply(BigDecimal.ONE.add(ratio));
    }
}
