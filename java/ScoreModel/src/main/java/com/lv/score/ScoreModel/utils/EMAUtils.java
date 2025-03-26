package com.lv.score.ScoreModel.utils;

import com.lv.score.ScoreModel.entity.StockStkFactorData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class EMAUtils {

    /**
     * 计算指定周期的指数移动平均(EMA)
     * @param data 历史数据列表(按日期升序排列，最新数据在末尾)
     * @param period 计算周期(如12、26等)
     * @return 最后一日的EMA值
     * @implNote 参考EMA标准计算公式[7,4](@ref): EMA = (今日收盘价 * α) + (昨日EMA * (1-α))，其中α=2/(period+1)
     */
    public static BigDecimal calculateEMA(List<StockStkFactorData> data, int period) {
        // 数据校验
        if (data.size() < period) {
            throw new IllegalArgumentException("数据量不足" + period + "日");
        }

        // 步骤1: 提取前复权收盘价序列(根据实体类字段定义)
        List<BigDecimal> closes = data.stream()
                .map(StockStkFactorData::getCloseQfq)
                .toList();

        // 步骤2: 计算初始EMA值(前period日的简单平均)[4](@ref)
        BigDecimal initialSum = closes.subList(0, period).stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal ema = initialSum.divide(BigDecimal.valueOf(period), 4, RoundingMode.HALF_UP);

        // 步骤3: 计算平滑系数α
        BigDecimal alpha = BigDecimal.valueOf(2)
                .divide(BigDecimal.valueOf(period + 1), 4, RoundingMode.HALF_UP);

        // 步骤4: 递归计算EMA值
        for (int i = period; i < closes.size(); i++) {
            BigDecimal price = closes.get(i);
            ema = price.multiply(alpha)
                    .add(ema.multiply(BigDecimal.ONE.subtract(alpha)));
            ema = ema.setScale(4, RoundingMode.HALF_UP); // 保留4位小数
        }

        return ema.setScale(2, RoundingMode.HALF_UP); // 最终保留2位小数
    }

    /**
     * 计算数值序列的EMA（用于DEA计算）
     * @param values 数值序列（按时间升序排列）
     * @param period 计算周期
     */
    public static BigDecimal calculateEMABigDecimal(List<BigDecimal> values, int period) {
        if (values.size() < period) {
            throw new IllegalArgumentException("数值序列长度不足" + period);
        }

        // 初始EMA为前period日的简单平均
        BigDecimal initialSum = values.subList(0, period).stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal ema = initialSum.divide(BigDecimal.valueOf(period), 4, RoundingMode.HALF_UP);

        // 计算平滑系数α
        BigDecimal alpha = BigDecimal.valueOf(2)
                .divide(BigDecimal.valueOf(period + 1), 4, RoundingMode.HALF_UP);

        // 递归计算EMA
        for (int i = period; i < values.size(); i++) {
            BigDecimal value = values.get(i);
            ema = value.multiply(alpha)
                    .add(ema.multiply(BigDecimal.ONE.subtract(alpha)));
            ema = ema.setScale(4, RoundingMode.HALF_UP);
        }

        return ema.setScale(2, RoundingMode.HALF_UP);
    }
}
