package com.lv.score.ScoreModel.stock_strategy.super_trend;

import com.lv.score.ScoreModel.entity.TradeDaily;
import com.lv.score.ScoreModel.stock_strategy.super_trend.entity.SuperTrendTradeSignal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 超级趋势（Super Trend）是一种基于波动性的技术分析指标，用于识别市场趋势方向和潜在转折点。它通过结合平均真实波幅（ATR）动态调整支撑和阻力线，帮助交易者做出买卖决策。
 * <p>
 * 核心原理
 * 动态通道：超级趋势在价格图表上形成一条动态线，位于价格上方（阻力）或下方（支撑），反映当前趋势。
 * <p>
 * 波动性适应：利用ATR调整通道宽度，市场波动大时通道变宽，减少假信号；波动小时通道收窄，提高灵敏度。
 * <p>
 * 计算公式与步骤
 * 中间价（Median Price）：
 * <p>
 * 中间价= (最高价 + 最低价) / 2
 * <p>
 * 计算ATR（平均真实波幅）：通常使用7或14周期。
 * <p>
 * 上下轨计算：
 * <p>
 * 上轨（空头趋势线）：中间价 + 乘数（如3）× ATR
 * <p>
 * 下轨（多头趋势线）：中间价 - 乘数 × ATR
 * <p>
 * 确定超级趋势线：
 * <p>
 * 初始值：首日超级趋势线为上轨或下轨，取决于价格位置。
 * <p>
 * 递归调整：
 * 若当前收盘价 > 前一日超级趋势线，则当前线为下轨（看涨）。
 * 若当前收盘价 < 前一日超级趋势线，则当前线为上轨（看跌）。
 * 否则维持前一日线。
 * <p>
 * 参数设置
 * ATR周期：常用7或14，影响波动性敏感度。
 * 乘数：常用3，决定通道宽度。乘数越大，信号越保守。
 * 应用方法
 * <p>
 * 趋势跟踪：
 * 买入信号：价格上穿超级趋势线（线切换至下轨，显示绿色）。
 * 卖出信号：价格下穿超级趋势线（线切换至上轨，显示红色）。
 * 动态止损：持有多头时，以下轨作为止损位；空头时以上轨止损。
 * <p>
 * 实例说明
 * 上升趋势：价格持续位于下轨（绿色线）上方，交易者持有多头。若价格跌破下轨，平仓并可能反手做空。
 * 下降趋势：价格受压于上轨（红色线），维持空头直至价格突破上轨。
 * 优缺点分析
 * <p>
 * 优点：
 * 自适应波动性，减少震荡市假信号。
 * 直观显示趋势方向，简化交易决策。
 * <p>
 * 缺点：
 * 滞后性：依赖历史数据，趋势反转时反应延迟。
 * 震荡市表现差：价格频繁穿越趋势线，导致多次亏损。
 * 与其他指标对比
 * 布林带：基于标准差，通道对称；超级趋势非对称，仅显示单侧支撑/阻力。
 * 移动平均线：超级趋势融入波动性，止损位更动态。
 * <p>
 * 优化建议
 * 结合其他指标：如RSI、MACD确认信号，避免假突破。
 * 参数调整：根据交易品种和时间框架测试不同乘数及ATR周期。
 * 超级趋势适用于趋势明显的市场，但在盘整行情中需谨慎使用。合理搭配其他工具，可提升交易策略的稳健性。
 *
 */
@Component
@Slf4j
public class SuperTrendStrategy {

    // 策略参数配置
    private static final int ATR_PERIOD = 14;
    private static final double MULTIPLIER = 5.0;
    private static final int REQUIRED_DAYS = 90; // 需要90天数据
    private static final int SIGNAL_DAYS = 3;    // 连续3天信号

    // 主分析方法
    public List<SuperTrendTradeSignal> analyze(List<TradeDaily> tradeDailies) {
        // 按股票代码分组并排序
        return tradeDailies.stream()
                .collect(Collectors.groupingBy(TradeDaily::getTsCode))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() >= REQUIRED_DAYS)
                .flatMap(entry -> processStock(entry.getKey(), entry.getValue()).stream())
                .collect(Collectors.toList());
    }

    private List<SuperTrendTradeSignal> processStock(String tsCode, List<TradeDaily> data) {
        List<TradeDaily> sortedData = data.stream()
                .sorted(Comparator.comparing(TradeDaily::getTradeDate))
                .collect(Collectors.toList());

        List<Double> highs = sortedData.stream().map(TradeDaily::getHigh).collect(Collectors.toList());
        List<Double> lows = sortedData.stream().map(TradeDaily::getLow).collect(Collectors.toList());
        List<Double> closes = sortedData.stream().map(TradeDaily::getClose).collect(Collectors.toList());

        List<Double> superTrend = calculateSuperTrend(highs, lows, closes);
        List<String> trendDirections = calculateTrendDirection(closes, superTrend);

        return generateSignals(tsCode, sortedData, trendDirections);
    }

    // 生成带日期的信号列表
    private List<SuperTrendTradeSignal> generateSignals(String tsCode,
                                              List<TradeDaily> data,
                                              List<String> directions) {
        List<SuperTrendTradeSignal> signals = new ArrayList<>();
        String prevAction = null;

        for (int i = 1; i < directions.size(); i++) { // 从第2天开始比较
            String current = directions.get(i);
            String previous = directions.get(i-1);

            if (!current.equals(previous)) {
                String action = current.equals("UP") ? "BUY" : "SELL";
                signals.add(new SuperTrendTradeSignal(
                        tsCode,
                        data.get(i).getTradeDate(),
                        action
                ));
            }
        }
        return signals;
    }

    // 判断每日趋势方向
    private List<String> calculateTrendDirection(List<Double> closes,
                                                 List<Double> superTrend) {
        List<String> directions = new ArrayList<>();
        for (int i = 0; i < closes.size(); i++) {
            directions.add(closes.get(i) > superTrend.get(i) ? "UP" : "DOWN");
        }
        return directions;
    }


    // Super Trend核心计算逻辑
    private List<Double> calculateSuperTrend(List<Double> highs,
                                             List<Double> lows,
                                             List<Double> closes) {
        int length = closes.size();
        List<Double> superTrend = new ArrayList<>();
        List<Double> atrValues = calculateATR(highs, lows, closes, ATR_PERIOD);

        double prevSuperTrend = 0;
        boolean prevTrendUp = false;

        for (int i = 0; i < length; i++) {
            if (i < ATR_PERIOD) {
                superTrend.add(0.0);
                continue;
            }

            double median = (highs.get(i) + lows.get(i)) / 2.0;
            double atr = atrValues.get(i);

            double upper = median + MULTIPLIER * atr;
            double lower = median - MULTIPLIER * atr;

            double currentClose = closes.get(i);

            if (i == ATR_PERIOD) { // 初始值
                prevSuperTrend = (currentClose > upper) ? lower : upper;
                prevTrendUp = currentClose > upper;
            } else {
                if (prevTrendUp) {
                    prevSuperTrend = (currentClose > prevSuperTrend) ?
                            Math.max(lower, prevSuperTrend) : upper;
                } else {
                    prevSuperTrend = (currentClose < prevSuperTrend) ?
                            Math.min(upper, prevSuperTrend) : lower;
                }
                prevTrendUp = currentClose > prevSuperTrend;
            }

            superTrend.add(prevSuperTrend);
        }

        return superTrend;
    }

    // 计算ATR指标
    private List<Double> calculateATR(List<Double> highs,
                                      List<Double> lows,
                                      List<Double> closes,
                                      int period) {
        int length = closes.size();
        List<Double> trValues = new ArrayList<>();
        List<Double> atrValues = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            if (i == 0) {
                trValues.add(highs.get(i) - lows.get(i));
                continue;
            }

            double tr1 = highs.get(i) - lows.get(i);
            double tr2 = Math.abs(highs.get(i) - closes.get(i-1));
            double tr3 = Math.abs(lows.get(i) - closes.get(i-1));
            trValues.add(Math.max(tr1, Math.max(tr2, tr3)));
        }

        // 计算ATR的移动平均
        for (int i = 0; i < length; i++) {
            if (i < period) {
                atrValues.add(0.0);
                continue;
            }

            double sum = 0;
            for (int j = i - period + 1; j <= i; j++) {
                sum += trValues.get(j);
            }
            atrValues.add(sum / period);
        }

        return atrValues;
    }


}
