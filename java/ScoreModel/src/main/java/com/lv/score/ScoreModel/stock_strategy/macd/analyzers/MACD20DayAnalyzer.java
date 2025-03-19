package com.lv.score.ScoreModel.stock_strategy.macd.analyzers;

import com.lv.score.ScoreModel.entity.StockStkFactorData;
import com.lv.score.ScoreModel.stock_strategy.macd.entity.MACD20DayAnalysisResult;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class MACD20DayAnalyzer {

    public static MACD20DayAnalysisResult analyze20Day(List<StockStkFactorData> last20DaysData) {
        Collections.reverse(last20DaysData);
        String tsCode = last20DaysData.get(0).getTsCode();
        if (last20DaysData.size() < 20) throw new IllegalArgumentException("数据不足20日, tsCode: " + tsCode);

        MACD20DayAnalysisResult result = new MACD20DayAnalysisResult();
        result.setTsCode(tsCode);
        result.setLastTradeDate(last20DaysData.get(last20DaysData.size() - 1).getTradeDate());

        // 1. 趋势强度分析（网页1、网页5）
        analyzeTrendStrength(last20DaysData, result);

        // 2. 双均线系统检测（网页1、网页4）
        checkDualMA(last20DaysData, result);

        // 3. 动量增强验证（网页1、网页3）
        validateMomentum(last20DaysData, result);

        // 4. 成交量共振分析（网页1、网页3）
        checkVolumeConfirmation(last20DaysData, result);

        // 5. 多指标过滤（网页2、网页5）
        applyMultiFilter(last20DaysData, result);

        return result;
    }

    // 趋势强度分析（包含20日均线斜率）
    private static void analyzeTrendStrength(List<StockStkFactorData> data, MACD20DayAnalysisResult result) {
        // 计算20日DIF均值作为趋势基准线
        BigDecimal difSum = data.stream()
                .map(StockStkFactorData::getMacdDif)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 增加零值判断
        if (difSum.compareTo(BigDecimal.ZERO) == 0) {
            result.setTrendDirection("FLAT");
            result.setTrendStrength(BigDecimal.ZERO);
            return;
        }

        BigDecimal dif20MA = difSum.divide(new BigDecimal(20), 2, BigDecimal.ROUND_HALF_UP);

        // 增加绝对值保护
        BigDecimal denominator = dif20MA.abs();
        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            result.setTrendDirection("FLAT");
            result.setTrendStrength(BigDecimal.ZERO);
            return;
        }

        // 判断趋势方向
        boolean isUpTrend = data.get(19).getMacdDif().compareTo(dif20MA) > 0;
        result.setTrendDirection(isUpTrend ? "UP" : "DOWN");

        // 安全计算趋势强度
        BigDecimal lastDif = data.get(19).getMacdDif();
        try {
            BigDecimal trendStrength = lastDif.subtract(dif20MA)
                    .divide(denominator, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100));
            result.setTrendStrength(trendStrength);
        } catch (ArithmeticException e) {
            result.setTrendStrength(BigDecimal.ZERO.setScale(4));  // 异常时保留小数位
        }
    }

    // 双均线系统检测（网页1中的MA20和MA5逻辑）
    private static void checkDualMA(List<StockStkFactorData> data, MACD20DayAnalysisResult result) {
        // 计算20日、5日均线（使用前复权价格）
        BigDecimal ma20 = data.stream()
                .map(StockStkFactorData::getCloseQfq)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(20), 2, BigDecimal.ROUND_HALF_UP);

        List<StockStkFactorData> last5Days = data.subList(15, 20);
        BigDecimal ma5 = last5Days.stream()
                .map(StockStkFactorData::getCloseQfq)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(5), 2, BigDecimal.ROUND_HALF_UP);

        // 判断均线排列（网页1中的VAR3增强）
        boolean isBullish = ma5.compareTo(ma20) > 0
                && data.get(19).getCloseQfq().compareTo(ma5) > 0;
        result.setMaAlignment(isBullish ? "BULLISH" : "BEARISH");
    }

    // 动量增强验证（网页1中的VAR1、VAR2逻辑）
    private static void validateMomentum(List<StockStkFactorData> data, MACD20DayAnalysisResult result) {
        // 连续三日MACD柱扩大（网页1的二次翻红升级版）
        boolean momentumFlag = true;
        for (int i = 17; i < 20; i++) { // 最后三日
            BigDecimal current = data.get(i).getMacd();
            BigDecimal prev = data.get(i - 1).getMacd();
            if (current.compareTo(prev) <= 0) {
                momentumFlag = false;
                break;
            }
        }
        result.setMomentumValid(momentumFlag);

        // MACD面积计算（网页5）
        BigDecimal macdArea = data.stream()
                .map(StockStkFactorData::getMacd)
                .filter(v -> v.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        result.setPositiveMacdArea(macdArea);
    }

    // 成交量共振分析（网页1中的成交量验证）
    private static void checkVolumeConfirmation(List<StockStkFactorData> data, MACD20DayAnalysisResult result) {
        // 计算20日平均成交量
        BigDecimal avgVol = data.stream()
                .map(StockStkFactorData::getVol)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(20), 2, BigDecimal.ROUND_HALF_UP);

        // 检测金叉日成交量突破（网页1的XG条件扩展）
        data.stream().filter(d -> isGoldenCrossDay(data, d))
                .findFirst()
                .ifPresent(goldenCrossDay -> {
                    boolean volumeBreak = goldenCrossDay.getVol().compareTo(avgVol) > 0;
                    result.setVolumeBreak(volumeBreak);
                });
    }

    // 多指标过滤（网页2、网页5）
    private static void applyMultiFilter(List<StockStkFactorData> data, MACD20DayAnalysisResult result) {
        StockStkFactorData latest = data.get(19);

        // KDJ未超买过滤（网页1的VAR2扩展）
        boolean kdjValid = latest.getKdjK().compareTo(new BigDecimal(80)) < 0
                && latest.getKdjD().compareTo(new BigDecimal(70)) < 0;

        // RSI强度过滤（网页3）
        boolean rsiValid = latest.getRsi12().compareTo(new BigDecimal(50)) > 0
                && latest.getRsi12().compareTo(latest.getRsi24()) > 0;

        result.setMultiFilterPassed(kdjValid && rsiValid);
    }

    private static boolean isGoldenCrossDay(List<StockStkFactorData> data, StockStkFactorData day) {
        int index = data.indexOf(day);
        if (index == 0) return false;

        StockStkFactorData prev = data.get(index - 1);
        return day.getMacdDif().compareTo(day.getMacdDea()) > 0
                && prev.getMacdDif().compareTo(prev.getMacdDea()) <= 0;
    }
}
