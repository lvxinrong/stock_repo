package com.lv.score.ScoreModel.stock_strategy.macd.selector;

import com.lv.score.ScoreModel.entity.StockStkFactorData;
import com.lv.score.ScoreModel.stock_strategy.macd.selector.enitiy.MACD_KDJ_SelectorResult;
import com.lv.score.ScoreModel.utils.EMAUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MACD_KDJ_Selector {

    public List<MACD_KDJ_SelectorResult> selectStocks(Map<String, List<StockStkFactorData>> stockDataMap) {
        return stockDataMap.entrySet().stream()
                .map(entry -> buildSelectionResult(entry.getKey(), entry.getValue()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private MACD_KDJ_SelectorResult buildSelectionResult(String tsCode, List<StockStkFactorData> dataList) {
        if (dataList.size() < 20) return null;

        // 获取最新数据
        StockStkFactorData latest = dataList.get(dataList.size() - 1);

        // 计算关键指标状态
        Map<String, Object> indicators = new HashMap<>();
        indicators.put("macd_dif", latest.getMacdDif());
        indicators.put("macd_dea", latest.getMacdDea());
        indicators.put("kdj_k", latest.getKdjK());
        indicators.put("kdj_j", latest.getKdjJ());

        // 构建结果对象
        return new MACD_KDJ_SelectorResult(
                tsCode,
                checkMACD_KDJ_Cross(dataList),    // 双金叉检测
                evaluateMACDTrend(dataList),      // 趋势强度分级
                latest.getKdjJ(),
                checkBullishDivergence(dataList), // 底背离检测
                indicators
        );
    }

    /**
     * 双金叉检测（改良版，参考网页1参数）
     */
    private boolean checkMACD_KDJ_Cross(List<StockStkFactorData> dataList) {
        StockStkFactorData latest = dataList.get(dataList.size() - 1);
        // 改良MACD参数：21周期EMA（参考网页1）
        BigDecimal dif21 = EMAUtils.calculateEMA(dataList, 21).subtract(EMAUtils.calculateEMA(dataList, 43));
        BigDecimal dea21 = EMAUtils.calculateEMABigDecimal(Collections.singletonList(dif21), 6);

        // 双金叉条件：改良MACD金叉 + KDJ金叉（J>50）
        boolean isMACDCross = dif21.compareTo(dea21) > 0;
        boolean isKDJCross = latest.getKdjK().compareTo(latest.getKdjD()) > 0
                && latest.getKdjJ().compareTo(BigDecimal.valueOf(50)) > 0;

        return isMACDCross && isKDJCross;
    }

    /**
     * MACD趋势分级（参考网页3归一化算法）
     */
    private String evaluateMACDTrend(List<StockStkFactorData> dataList) {
        // 计算MACD动能指数（0-100）
        BigDecimal maxMACD = dataList.stream().map(StockStkFactorData::getMacd)
                .max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal minMACD = dataList.stream().map(StockStkFactorData::getMacd)
                .min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal macdRange = maxMACD.subtract(minMACD);
        BigDecimal normalized = dataList.get(dataList.size() - 1).getMacd().subtract(minMACD)
                .divide(macdRange, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        // 分级逻辑
        if (normalized.compareTo(BigDecimal.valueOf(70)) > 0) return "STRONG_BULL";
        else if (normalized.compareTo(BigDecimal.valueOf(30)) > 0) return "WEAK_BULL";
        else return "NEUTRAL";
    }

    /**
     * 底背离检测（增强版，参考网页2的DEA融合）
     */
    private boolean checkBullishDivergence(List<StockStkFactorData> dataList) {
        // 获取价格最低点和对应MACD值
        int minPriceIndex = findMinIndex(dataList, StockStkFactorData::getLowQfq);
        BigDecimal minPriceMACD = dataList.get(minPriceIndex).getMacd();

        // 当前MACD值需高于最低点且DEA上穿（参考网页2）
        StockStkFactorData latest = dataList.get(dataList.size() - 1);
        boolean isMACDHigher = latest.getMacd().compareTo(minPriceMACD) > 0;
        boolean isDEACross = latest.getMacdDea().compareTo(dataList.get(dataList.size()-2).getMacdDea()) > 0;

        return isMACDHigher && isDEACross;
    }

    public static int findMinIndex(List<StockStkFactorData> data,
                                   Function<StockStkFactorData, BigDecimal> extractor) {
        if (data.isEmpty()) return -1;

        int minIndex = 0;
        BigDecimal minValue = extractor.apply(data.get(0));

        for (int i = 1; i < data.size(); i++) {
            BigDecimal current = extractor.apply(data.get(i));
            if (current == null) continue;

            if (current.compareTo(minValue) < 0) {
                minValue = current;
                minIndex = i;
            }
        }
        return minIndex;
    }

}
