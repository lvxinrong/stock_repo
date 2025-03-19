package com.lv.score.ScoreModel.stock_strategy.macd.analyzers;

import com.lv.score.ScoreModel.entity.StockStkFactorData;
import com.lv.score.ScoreModel.stock_strategy.macd.entity.MACD5DayAnalysisResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class MACD5DayAnalyzer {


    /**
     * 分析近5日MACD特征
     * @param last5DaysData 按日期升序排列的最近5日数据
     * @return 包含关键信号的DTO
     */
    public static MACD5DayAnalysisResult analyzeLast5Days(List<StockStkFactorData> last5DaysData) {
        if (last5DaysData.size() < 5) {
            throw new IllegalArgumentException("数据不足5日");
        }

        MACD5DayAnalysisResult analysisResult = new MACD5DayAnalysisResult();

        // 1. 金叉/死叉检测
        detectCrossEvents(last5DaysData, analysisResult);

        // 2. 柱状图动能分析
        analyzeMACDHistogram(last5DaysData, analysisResult);

        // 3. 零轴位置分析
        analyzeZeroAxisPosition(last5DaysData, analysisResult);

        // 4. 背离检测（需价格数据配合）
        detectTopDivergence(last5DaysData, analysisResult);

        detectBottomDivergence(last5DaysData, analysisResult);

        return analysisResult;
    }

    /**
     * 金叉， 死叉检测
     */
    private static void detectCrossEvents(List<StockStkFactorData> data, MACD5DayAnalysisResult analysisResult) {

        for (int i = 1; i < data.size(); i++) {
            StockStkFactorData prev = data.get(i - 1);
            StockStkFactorData curr = data.get(i);

            // 金叉条件： 当日DIF > DEA 且前一日 DIF < DEA
            if (curr.getMacdDif().compareTo(curr.getMacdDea()) > 0
                    && prev.getMacdDif().compareTo(prev.getMacdDea()) <= 0) {
                analysisResult.getGoldenCrossDays().add(curr.getTradeDate());
            }

            // 死叉条件: 逻辑相反
            if (curr.getMacdDif().compareTo(curr.getMacdDea()) < 0
                    && prev.getMacdDif().compareTo(prev.getMacdDea()) >= 0) {
                analysisResult.getDeadCrossDays().add(curr.getTradeDate());
            }
        }

    }

    // 柱状图动能分析
    public static void analyzeMACDHistogram(List<StockStkFactorData> data, MACD5DayAnalysisResult analysisResult) {
        BigDecimal[] macdValues = data.stream()
                .map(StockStkFactorData::getMacd)
                .toArray(BigDecimal[]::new);

        // 检测二次翻红：连续两日 MACD 柱增长且>0（网页2）
        for (int i = 2; i < macdValues.length; i++) {
            if (macdValues[i].compareTo(macdValues[i - 1]) > 0
                    && macdValues[i - 1].compareTo(macdValues[i - 2]) > 0
                    && macdValues[i].compareTo(BigDecimal.ZERO) > 0) {
                analysisResult.setDoubleRed(true);
            }
        }

        // 计算5日柱状图平均值
        BigDecimal sum = BigDecimal.ZERO;
        for(BigDecimal v : macdValues) sum = sum.add(v);
        analysisResult.setAvgMacdHistogram(sum.divide(new BigDecimal(5), 2,  RoundingMode.HALF_UP));
    }

    // 零轴位置分析
    private static void analyzeZeroAxisPosition(List<StockStkFactorData> data, MACD5DayAnalysisResult analysisResult) {
        long daysAboveZero = data.stream()
                .filter(d -> d.getMacdDif().compareTo(BigDecimal.ZERO) > 0)
                .count();
        analysisResult.setDaysAboveZero(daysAboveZero);
    }

    /**
     * 顶背离检测（价格新高但MACD未新高）
     */
    private static void detectTopDivergence(List<StockStkFactorData> data, MACD5DayAnalysisResult result) {
        if (data.size() < 2) return; // 至少需要2日数据

        // 1. 获取历史最高价和对应MACD值（网页4、网页6）
        BigDecimal maxPrice = data.stream()
                .map(StockStkFactorData::getCloseHfq)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        BigDecimal maxMacd = data.stream()
                .map(StockStkFactorData::getMacd)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        // 2. 判断当前是否为价格新高且MACD未新高（网页2、网页7）
        StockStkFactorData latest = data.get(data.size()-1);
        boolean isPricePeak = latest.getCloseHfq().compareTo(maxPrice) == 0;
        boolean isMacdWeak = latest.getMacd().compareTo(maxMacd) < 0;

        result.setTopDivergence(isPricePeak && isMacdWeak);
    }

    /**
     * 底背离检测（价格新低但MACD未新低）
     */
    private static void detectBottomDivergence(List<StockStkFactorData> data, MACD5DayAnalysisResult result) {
        if (data.size() < 2) return;

        // 1. 获取历史最低价和对应MACD值（网页4、网页8）
        BigDecimal minPrice = data.stream()
                .map(StockStkFactorData::getCloseHfq)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        BigDecimal minMacd = data.stream()
                .map(StockStkFactorData::getMacd)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        // 2. 判断当前是否为价格新低且MACD未新低（网页5、网页6）
        StockStkFactorData latest = data.get(data.size()-1);
        boolean isPriceTrough = latest.getCloseHfq().compareTo(minPrice) == 0;
        boolean isMacdStrong = latest.getMacd().compareTo(minMacd) > 0;

        result.setBottomDivergence(isPriceTrough && isMacdStrong);
    }

}
