package com.lv.score.ScoreModel.utils;

import java.math.BigDecimal;

public class DataValidator {
    // 处理null值（将null转为0）
    public static BigDecimal nullToZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    // 验证历史波动区间合理性
    public static boolean isValidRange(BigDecimal hisLow, BigDecimal hisHigh) {
        return hisLow.compareTo(hisHigh) < 0;
    }
}
