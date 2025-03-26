package com.lv.score.ScoreModel.stock_strategy.stock_cyq.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ChipConcentrationCalculator {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final RoundingMode ROUND_HALF_UP = RoundingMode.HALF_UP;

    /**
     * 计算C70
     *  ((cost85Pct - cost15Pct) / weightAvg) * 100;
     */
    public static BigDecimal calculateC70(BigDecimal cost15Pct, BigDecimal cost85Pct, BigDecimal weightAvg) {
        if (weightAvg.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("加权平均成本不能为0");
        }
        BigDecimal numerator = cost85Pct.subtract(cost15Pct);
        return numerator.divide(weightAvg, 4, ROUND_HALF_UP).multiply(HUNDRED);
    }

    /**
     * 计算C90指标（保留4位小数）
     *  ((cost95pct - cost5pct) / weightAvg) * 100
     */
    public static BigDecimal calculateC90(BigDecimal cost5Pct, BigDecimal cost95Pct, BigDecimal weightAvg) {
        if (weightAvg.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("加权平均成本不能为0");
        }
        BigDecimal numerator = cost95Pct.subtract(cost5Pct);
        return numerator.divide(weightAvg, 4, ROUND_HALF_UP).multiply(HUNDRED);
    }
}
