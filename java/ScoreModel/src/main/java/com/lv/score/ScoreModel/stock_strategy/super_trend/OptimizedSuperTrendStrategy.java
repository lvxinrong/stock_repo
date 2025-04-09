package com.lv.score.ScoreModel.stock_strategy.super_trend;

import com.lv.score.ScoreModel.entity.TradeDaily;
import com.lv.score.ScoreModel.service.ITradeDailyService;
import com.lv.score.ScoreModel.stock_strategy.super_trend.entity.SuperTrendTradeSignal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 参数说明与推荐范围：
 * <p>
 * 1. atrPeriod（ATR周期）
 * - 作用：决定波动率计算的灵敏度
 * - 范围：5-20日
 * - 推荐：短线策略用5-10，长线用14-20
 * <p>
 * 2. multiplier（通道乘数）
 * - 作用：控制通道宽度，过滤假突破
 * - 范围：1.5（敏感）-5.0（保守）
 * - 推荐：股票2.5-3.5，加密货币3.0-4.0
 * <p>
 * 3. minTrendDays（趋势确认天数）
 * - 作用：确认趋势持续性的最小天数
 * - 范围：1-5日
 * - 推荐：A股市场建议3日（过滤短期震荡）
 * <p>
 * 4. requiredDays（数据天数）
 * - 作用：策略运行需要的最小历史数据量
 * - 范围：至少是atrPeriod的5倍（推荐60-200）
 * <p>
 * 5. minVolumeRatio（成交量比率）
 * - 作用：验证信号时的成交量放大倍数
 * - 范围：1.0（不限制）-2.0（高要求）
 * - 推荐：结合市场流动性，1.2-1.5较合适
 * <p>
 *
 * 参数优化建议表
 * 市场类型	ATR周期	乘数	 趋势天数	 成交量比率	    适用场景
 * 趋势市场	10-14	2.8	  3	       1.2	     单边上涨/下跌行情
 * 震荡市场	7-10	3.5	  2	       1.5	     箱体震荡行情
 * 突破性市场	5-7	    2.0	  1	       1.8	     重大事件驱动行情
 * 加密货币	14-18	3.8	  3	       1.1	     高波动性数字资产
 * 蓝筹股	14-20	3.0	  4	       1.3	     大盘股稳健交易
 * <p>
 */
@Slf4j
@Component
public class OptimizedSuperTrendStrategy {

    @Autowired
    ITradeDailyService tradeDailyService;

    /**
     * 参数优化建议表
     * 市场类型	ATR周期	乘数	 趋势天数	 成交量比率	    适用场景
     * 趋势市场	10-14	2.8	  3	       1.2	     单边上涨/下跌行情
     * 震荡市场	7-10	3.5	  2	       1.5	     箱体震荡行情
     * 突破性市场	5-7	    2.0	  1	       1.8	     重大事件驱动行情
     * 加密货币	14-18	3.8	  3	       1.1	     高波动性数字资产
     * 蓝筹股	14-20	3.0	  4	       1.3	     大盘股稳健交易
     * <p>
     */

    private int atrPeriod = 10;          // ATR计算周期 [5-20]
    private double multiplier = 2.5;     // 通道乘数 [1.5-5.0]
    private int minTrendDays = 3;        // 趋势确认天数 [1-5]
    private int requiredDays = 90;       // 最小数据天数 [60-200]
    private double minVolumeRatio = 1.2; // 成交量比率 [1.0-2.0]

    // 参数设置方法（带校验）
    public void setAtrPeriod(int period) {
        if (period < 5 || period > 20) {
            throw new IllegalArgumentException("ATR周期应在5-20之间");
        }
        this.atrPeriod = period;
    }

    public void setMultiplier(double multiplier) {
        if (multiplier < 1.5 || multiplier > 5.0) {
            throw new IllegalArgumentException("乘数应在1.5-5.0之间");
        }
        this.multiplier = multiplier;
    }

    public void setMinTrendDays(int days) {
        if (days < 1 || days > 5) {
            throw new IllegalArgumentException("趋势确认天数应在1-5之间");
        }
        this.minTrendDays = days;
    }

    public void setRequiredDays(int days) {
        if (days < 60 || days > 200) {
            throw new IllegalArgumentException("数据天数应在60-200之间");
        }
        this.requiredDays = days;
    }

    public void setMinVolumeRatio(double ratio) {
        if (ratio < 1.0 || ratio > 2.0) {
            throw new IllegalArgumentException("成交量比率应在1.0-2.0之间");
        }
        this.minVolumeRatio = ratio;
    }


    public List<SuperTrendTradeSignal> analyze(List<TradeDaily> tradeDailies) {
        long startTime = System.currentTimeMillis();

        String latestDate = tradeDailyService.getLatestDate();

        List<SuperTrendTradeSignal> signals = tradeDailies.stream()
                .collect(Collectors.groupingBy(TradeDaily::getTsCode))
                .entrySet().parallelStream()
                .filter(entry -> validateStockData(entry.getValue(), latestDate))
                .flatMap(entry -> processStock(entry.getKey(), entry.getValue()).stream())
                .sorted(Comparator.comparing(SuperTrendTradeSignal::getTradeDate))
                .collect(Collectors.toList());

        log.info("策略执行耗时：{}ms", System.currentTimeMillis() - startTime);
        return signals;
    }

    private boolean validateStockData(List<TradeDaily> data, String latestDate) {
        if (data.size() < requiredDays) return false;
        if (!data.get(0).getTradeDate().equals(latestDate)) {
            log.warn("当前股票交易数据不是最新，可能是退市股票不参与计算 tsCode: {}", data.get(0).getTsCode());
            return false;
        }
        // 数据质量校验
        return data.stream().noneMatch(d ->
                d.getVol() == null || d.getVol() == 0 ||
                        d.getHigh() < d.getLow() ||
                        d.getClose() < d.getLow() || d.getClose() > d.getHigh()
        );
    }

    private List<SuperTrendTradeSignal> processStock(String tsCode, List<TradeDaily> data) {
        List<TradeDaily> sortedData = data.stream()
                .sorted(Comparator.comparing(TradeDaily::getTradeDate))
                .collect(Collectors.toList());

        // 提取价格序列
        List<Double> highs = sortedData.stream()
                .map(TradeDaily::getHigh)
                .collect(Collectors.toList());
        List<Double> lows = sortedData.stream()
                .map(TradeDaily::getLow)
                .collect(Collectors.toList());
        List<Double> closes = sortedData.stream()
                .map(TradeDaily::getClose)
                .collect(Collectors.toList());
        List<Double> volumes = sortedData.stream()
                .map(TradeDaily::getVol)
                .collect(Collectors.toList());

        // 计算技术指标
        List<Double> atr = calculateSMA(calculateTR(highs, lows, closes), atrPeriod);
        List<Double> superTrend = calculateSuperTrend(highs, lows, closes, atr);
        List<Double> volumeMA = calculateSMA(volumes, 20);

        return generateSignals(tsCode, sortedData, closes, superTrend, atr, volumeMA);
    }

    // 核心改进1：修正SuperTrend计算逻辑
    private List<Double> calculateSuperTrend(List<Double> highs,
                                             List<Double> lows,
                                             List<Double> closes,
                                             List<Double> atr) {
        List<Double> superTrend = new ArrayList<>(Collections.nCopies(closes.size(), 0.0));
        boolean isUptrend = false;
        double prevSuperTrend = 0;

        for (int i = 0; i < closes.size(); i++) {
            if (i < atrPeriod) {
                superTrend.set(i, closes.get(i));
                continue;
            }

            double median = (highs.get(i) + lows.get(i)) / 2;
            double basicUpper = median + multiplier * atr.get(i);
            double basicLower = median - multiplier * atr.get(i);

            if (i == atrPeriod) {
                isUptrend = closes.get(i) > basicUpper;
                prevSuperTrend = isUptrend ? basicLower : basicUpper;
            } else {
                // 动态调整逻辑
                if (isUptrend) {
                    prevSuperTrend = Math.max(basicLower, prevSuperTrend);
                    if (closes.get(i) < prevSuperTrend) {
                        isUptrend = false;
                        prevSuperTrend = basicUpper;
                    }
                } else {
                    prevSuperTrend = Math.min(basicUpper, prevSuperTrend);
                    if (closes.get(i) > prevSuperTrend) {
                        isUptrend = true;
                        prevSuperTrend = basicLower;
                    }
                }
            }
            superTrend.set(i, prevSuperTrend);
        }
        return superTrend;
    }

    // 核心改进2：增强信号生成逻辑
    private List<SuperTrendTradeSignal> generateSignals(String tsCode,
                                                        List<TradeDaily> data,
                                                        List<Double> closes,
                                                        List<Double> superTrend,
                                                        List<Double> atr,
                                                        List<Double> volumeMA) {
        List<SuperTrendTradeSignal> signals = new ArrayList<>();
        int trendDuration = 0;
        boolean inPosition = false;
        double lastBuyPrice = 0;

        for (int i = atrPeriod; i < data.size(); i++) {
            TradeDaily daily = data.get(i);
            boolean isAbove = closes.get(i) > superTrend.get(i);
            boolean volumeCondition = daily.getVol() > volumeMA.get(i) * minVolumeRatio;
            boolean atrCondition = atr.get(i) > atr.subList(i-5, i).stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0);

            // 趋势持续计数器
            trendDuration = isAbove ? trendDuration + 1 : 0;

            // 生成买入信号
            if (!inPosition && isAbove && trendDuration >= minTrendDays
                    && volumeCondition && atrCondition) {
                signals.add(new SuperTrendTradeSignal(
                        tsCode, daily.getTradeDate(), "BUY",
                        closes.get(i), superTrend.get(i)
                ));
                inPosition = true;
                lastBuyPrice = closes.get(i);
                trendDuration = 0;
            }

            // 生成卖出信号（动态止损）
            if (inPosition) {
                double currentAtr = atr.get(i);
                double stopLoss = lastBuyPrice - 2 * currentAtr;
                boolean stopHit = closes.get(i) < stopLoss;
                boolean trendBreak = closes.get(i) < superTrend.get(i);

                if (stopHit || trendBreak) {
                    signals.add(new SuperTrendTradeSignal(
                            tsCode, daily.getTradeDate(), "SELL",
                            closes.get(i), superTrend.get(i)
                    ));
                    inPosition = false;
                }
            }
        }
        return signals;
    }

    // 核心改进3：优化ATR计算
    private List<Double> calculateTR(List<Double> highs,
                                     List<Double> lows,
                                     List<Double> closes) {
        List<Double> tr = new ArrayList<>();
        for (int i = 0; i < highs.size(); i++) {
            if (i == 0) {
                tr.add(highs.get(i) - lows.get(i));
            } else {
                double tr1 = highs.get(i) - lows.get(i);
                double tr2 = Math.abs(highs.get(i) - closes.get(i-1));
                double tr3 = Math.abs(lows.get(i) - closes.get(i-1));
                tr.add(Math.max(tr1, Math.max(tr2, tr3)));
            }
        }
        return tr;
    }

    // 核心改进4：高效移动平均计算
    private List<Double> calculateSMA(List<Double> data, int period) {
        List<Double> sma = new ArrayList<>();
        double sum = 0;

        for (int i = 0; i < data.size(); i++) {
            sum += data.get(i);
            if (i >= period) sum -= data.get(i - period);

            if (i >= period - 1) {
                sma.add(sum / period);
            } else {
                sma.add(0.0);
            }
        }
        return sma;
    }
}
