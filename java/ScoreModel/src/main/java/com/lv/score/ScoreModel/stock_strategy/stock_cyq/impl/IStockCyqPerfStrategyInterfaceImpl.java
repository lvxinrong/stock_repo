package com.lv.score.ScoreModel.stock_strategy.stock_cyq.impl;

import com.lv.score.ScoreModel.entity.DailyBasicInfo;
import com.lv.score.ScoreModel.entity.StockCyqPerfDay;
import com.lv.score.ScoreModel.service.IDailyBasicInfoService;
import com.lv.score.ScoreModel.service.IStockBasicService;
import com.lv.score.ScoreModel.service.IStockCyqPerfDayService;
import com.lv.score.ScoreModel.stock_strategy.stock_cyq.IStockCyqPerfStrategyInterface;
import com.lv.score.ScoreModel.stock_strategy.stock_cyq.entity.StockCyqConcentratedResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 集中度阈值
 * 保守策略：C70 < 15 且 C90 < 15（极端集中，适合长线）
 * 平衡策略：C70 < 20 且 C90 < 20（兼顾安全性与机会频率）
 * 动态调整：小盘股（<50亿市值）可放宽至20，大盘股（>200亿）收紧至12。
 *
 * 行业联动：同一行业若多只个股筹码集中，可能预示板块性机会。
 * 风险提示：高位筹码集中（如股价接近cost_95pct）可能是主力出货，需对比历史筹码分布形态。
 */
@Service
@Slf4j
public class IStockCyqPerfStrategyInterfaceImpl implements IStockCyqPerfStrategyInterface {

    private static final BigDecimal THRESHOLD = BigDecimal.valueOf(15.0); // 集中度阈值

    @Autowired
    IStockCyqPerfDayService iStockCyqPerfDayService;

    @Autowired
    IStockBasicService iStockBasicService;

    @Autowired
    IDailyBasicInfoService dailyBasicInfoService;

    private final static MathContext mc = new MathContext(8, RoundingMode.HALF_UP);



    @Override
    public List<String> getGoodStockCyqPerfLatest5Days() {
        List<String> selectedStocks = new ArrayList<>();
        Map<String, List<BigDecimal>> stockConcentrations = new HashMap<>();

        // 查询最近5个交易日的股票筹码分布
        List<StockCyqPerfDay> data = iStockCyqPerfDayService.getLatest5DaysData();
        for (StockCyqPerfDay stock : data) {
            stockConcentrations.putIfAbsent(stock.getTsCode(), new ArrayList<>());
            List<BigDecimal> temp = stockConcentrations.get(stock.getTsCode());
            BigDecimal c70 = ChipConcentrationCalculator.calculateC70(stock.getCost15pct(), stock.getCost85pct(), stock.getWeightAvg());
            BigDecimal c90 = ChipConcentrationCalculator.calculateC90(stock.getCost5pct(), stock.getCost95pct(), stock.getWeightAvg());
            BigDecimal value = c90.add(c70);
            temp.add(value.divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP));
        }

        for(Map.Entry<String, List<BigDecimal>> entry : stockConcentrations.entrySet()) {
            String tsCode = entry.getKey();
            List<BigDecimal> concentrations = entry.getValue();
            if (concentrations.size() < 5) {
                continue;
            }
            // 方案1 严格模式 连续5日达标
            boolean allPass = true;
            for(BigDecimal c : concentrations) {
                if (c.compareTo(THRESHOLD) > 0) {
                    allPass = false;
                    break;
                }
            }
            if (allPass) {
                selectedStocks.add(tsCode);
            }
//            // 方案2 宽松模式 均值达标
//            BigDecimal average = concentrations.isEmpty() ?
//                    BigDecimal.ZERO :  // 空集合返回0
//                    concentrations.stream()
//                            .reduce(BigDecimal.ZERO, BigDecimal::add)
//                            .divide(
//                                    BigDecimal.valueOf(concentrations.size()),
//                                    4,
//                                    RoundingMode.HALF_UP
//                            );
//
//            if (average.compareTo(THRESHOLD) < 0) {
//                selectedStocks.add(tsCode);
//            }

        }
        return selectedStocks;
    }

    @Override
    public List<String> getGoodStockCyqPerfLatest20Days() {
        List<String> selectedStocks = new ArrayList<>();
        Map<String, List<BigDecimal>> stockConcentrations = new HashMap<>();

        // 查询最近5个交易日的股票筹码分布
        List<StockCyqPerfDay> data = iStockCyqPerfDayService.getLatest20DaysData();
        for (StockCyqPerfDay stock : data) {
            stockConcentrations.putIfAbsent(stock.getTsCode(), new ArrayList<>());
            List<BigDecimal> temp = stockConcentrations.get(stock.getTsCode());
            BigDecimal c70 = ChipConcentrationCalculator.calculateC70(stock.getCost15pct(), stock.getCost85pct(), stock.getWeightAvg());
            BigDecimal c90 = ChipConcentrationCalculator.calculateC90(stock.getCost5pct(), stock.getCost95pct(), stock.getWeightAvg());
            BigDecimal value = c90.add(c70);
            temp.add(value.divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP));
        }

        for(Map.Entry<String, List<BigDecimal>> entry : stockConcentrations.entrySet()) {
            String tsCode = entry.getKey();
            List<BigDecimal> concentrations = entry.getValue();
            if (concentrations.size() < 20) {
                continue;
            }
            // 方案1 严格模式 连续20日达标
            boolean allPass = true;
            for (BigDecimal c : concentrations) {
                if (c.compareTo(THRESHOLD) > 0) {
                    allPass = false;
                    break;
                }
            }
            if (allPass) {
                selectedStocks.add(tsCode);
            }
        }
        return selectedStocks;
    }

    /**
     *一、核心指标设计（需计算每日特征）
     * 成本区间收敛度
     * (cost95pct - cost5pct)/hisHigh
     * 意义：衡量筹码分布范围占历史价格区间的比例，数值越小说明筹码越集中
     * 主力控盘强度
     * (cost85pct - cost15pct)/weightAvg
     * 意义：中间70%筹码的集中程度，数值持续下降表明主力在收窄控盘区间
     * 成本重心偏移率
     * ABS(cost50pct - weightAvg)/weightAvg
     * 意义：中位数成本与加权成本的偏离度，数值越小表明筹码结构越对称
     * 多空博弈系数
     * (cost50pct - cost15pct)/(cost85pct - cost50pct)
     * 意义：若系数>1说明下方支撑强，<1则上方压力大
     * 二、筛选逻辑（需满足连续趋势）
     * 收敛趋势验证
     * 计算20日成本区间收敛度的移动标准差（窗口5日），要求标准差持续下降
     * 最近5日收敛度环比降幅>10%
     * 主力行为验证
     * 主力控盘强度连续10日下降
     * 期间出现至少3日 cost85pct - cost15pct < 0.5*weightAvg
     * 结构稳定性验证
     * 成本重心偏移率20日均值<5%
     * 多空博弈系数连续5日处于[0.8,1.2]区间
     * 量价配合验证
     * 胜率(winnerRate)20日均值>55%且呈上升趋势
     * 当前价格处于cost15pct~cost85pct区间
     */
    @Override
    public List<StockCyqConcentratedResult> getLast20DaysStock() {
        List<StockCyqConcentratedResult> result = new ArrayList<>();
        //获取过去20个交易日所有的股票筹码结构
        String lastDate = iStockCyqPerfDayService.latestDay();
        List<StockCyqPerfDay> data = iStockCyqPerfDayService.getLatest20DaysData();
        Map<String, List<StockCyqPerfDay>> stockCyqPerfDayMap = data.stream().collect(Collectors.groupingBy(StockCyqPerfDay::getTsCode));
        //分类 Map<tsCode, List<TradeData>>
        for (Map.Entry<String, List<StockCyqPerfDay>> entry : stockCyqPerfDayMap.entrySet()) {
            try {
                if (lastDate.equals(entry.getValue().get(0).getTradeDate())) {
                    String tsCode = entry.getKey();
                    List<StockCyqPerfDay> analyzeDataList = entry.getValue();
                    // desc limit后返回的数据，最新的数据在0的位置，需要翻转一下
                    Collections.reverse(analyzeDataList);
                    DailyBasicInfo dailyBasicInfo = dailyBasicInfoService.getLatestData(tsCode);
                    StockCyqConcentratedResult analyzeResult = analyze(analyzeDataList, BigDecimal.valueOf(dailyBasicInfo.getClose()));
                    analyzeResult.setCurrentPrice(BigDecimal.valueOf(dailyBasicInfo.getClose()));
                    analyzeResult.setLastTradeDate(dailyBasicInfo.getTradeDate());
                    analyzeResult.setLastDailyBasicInfo(dailyBasicInfo);
                    result.add(analyzeResult);
                } else {
                    log.warn("股票筹码分布数据不是最新的，忽略。 tsCode: {}", entry.getKey());
                }
            } catch (Exception e) {
                log.error("getLast20DaysStock Exception. tsCode: {}", entry.getKey(), e);
            }
        }
        return result;
    }

    private StockCyqConcentratedResult analyze(List<StockCyqPerfDay> data, BigDecimal currentPrice) {
        List<BigDecimal> convergeRatios = data.stream()
                .map(d -> d.getCost95pct().subtract(d.getCost5pct(), mc)
                        .divide(d.getHisHigh(), mc))
                .toList();

        List<BigDecimal> controlStrengths = data.stream()
                .map(d -> d.getCost85pct().subtract(d.getCost15pct(), mc)
                        .divide(d.getWeightAvg(), mc))
                .toList();


        // 收敛趋势验证
        boolean convergeValid = isMonotonicDecreasing(
                IntStream.rangeClosed(5, 20)
                        .mapToObj(i -> rollingStd(convergeRatios, i))
                        .collect(Collectors.toList()));

        // 主力验证
        boolean controlValid = isMonotonicDecreasing(
                controlStrengths.subList(controlStrengths.size() - 10, controlStrengths.size()));

        // 量价验证
        boolean priceValid = currentPrice.compareTo(data.get(data.size() - 1).getCost15pct()) > 0
                && currentPrice.compareTo(data.get(data.size() - 1).getCost85pct()) < 0;
        StockCyqConcentratedResult stockCyqConcentratedResult = new StockCyqConcentratedResult();
        stockCyqConcentratedResult.setTsCode(data.get(0).getTsCode());
        stockCyqConcentratedResult.setConvergeRatio(convergeRatios);
        stockCyqConcentratedResult.setLastConvergeRatio(convergeRatios.get(convergeRatios.size() - 1));
        stockCyqConcentratedResult.setGood(convergeValid && controlValid && priceValid);
        stockCyqConcentratedResult.setConvergeValid(convergeValid);
        stockCyqConcentratedResult.setControlValid(controlValid);
        stockCyqConcentratedResult.setPriceValid(priceValid);
        return stockCyqConcentratedResult;
    }

    // 判断是否单调递减（网页1趋势判断的Java实现）
    private boolean isMonotonicDecreasing(List<BigDecimal> list) {
        for(int i=1; i<list.size(); i++) {
            if(list.get(i).compareTo(list.get(i-1)) > 0) return false;
        }
        return true;
    }

    // 计算20日移动标准差（网页3提到的统计方法）
    private BigDecimal rollingStd(List<BigDecimal> data, int window) {
        List<BigDecimal> windowData = data.subList(data.size() - window, data.size());
        double avg = windowData.stream().mapToDouble(BigDecimal::doubleValue).average().orElse(0);
        double variance = windowData.stream()
                .mapToDouble(v -> Math.pow(v.doubleValue() - avg, 2))
                .average().orElse(0);
        return BigDecimal.valueOf(Math.sqrt(variance));
    }

}
