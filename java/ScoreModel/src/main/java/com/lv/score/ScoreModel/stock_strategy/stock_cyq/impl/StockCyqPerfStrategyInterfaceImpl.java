package com.lv.score.ScoreModel.stock_strategy.stock_cyq.impl;

import com.lv.score.ScoreModel.entity.StockBasic;
import com.lv.score.ScoreModel.entity.StockCyqPerfDay;
import com.lv.score.ScoreModel.service.IStockBasicService;
import com.lv.score.ScoreModel.service.IStockCyqPerfDayService;
import com.lv.score.ScoreModel.stock_strategy.stock_cyq.ChipConcentrationCalculator;
import com.lv.score.ScoreModel.stock_strategy.stock_cyq.StockCyqPerfStrategyInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class StockCyqPerfStrategyInterfaceImpl implements StockCyqPerfStrategyInterface {

    private static final BigDecimal THRESHOLD = BigDecimal.valueOf(15.0); // 集中度阈值

    @Autowired
    IStockCyqPerfDayService iStockCyqPerfDayService;

    @Autowired
    IStockBasicService iStockBasicService;


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
}
