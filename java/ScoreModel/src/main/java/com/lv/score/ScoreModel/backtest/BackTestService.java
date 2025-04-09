package com.lv.score.ScoreModel.backtest;

import com.lv.score.ScoreModel.entity.TradeDaily;
import com.lv.score.ScoreModel.service.IStockBasicService;
import com.lv.score.ScoreModel.service.ITradeDailyService;
import com.lv.score.ScoreModel.stock_strategy.super_trend.OptimizedSuperTrendStrategy;
import com.lv.score.ScoreModel.stock_strategy.super_trend.entity.SuperTrendTradeSignal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BackTestService {

    @Autowired
    ITradeDailyService tradeDailyService;


    @Autowired
    OptimizedSuperTrendStrategy optimizedSuperTrendStrategy;

    @Autowired
    IStockBasicService iStockBasicService;

    /**
     * @param tsCode    股票代码
     * @param startDate 买入时间
     * @param endDate   卖出时间
     * @return 收益率计算 (end.close - start.open) / start.open
     */
    public double calBackTestByTsCode(String tsCode, String startDate, String endDate) {
        TradeDaily start = tradeDailyService.getTradeDailyNext(tsCode, startDate);
        if (start == null) {
            log.info("tsCode:{} start is null. startDate:{}, endDate: {}", tsCode, startDate, endDate);
            return 0.0;
        }
        TradeDaily end;
        if (endDate.equals("latest")) {
            end = tradeDailyService.getLastData(tsCode);
        } else {
            end = tradeDailyService.getTradeDailyNext(tsCode, endDate);
        }

        if (end == null) {
            // end == null 的情况下，取最新的
            log.info("end 数据为空，取最新的交易数据， tsCode: {}, startDate: {}, endDate: {}", tsCode, startDate, endDate);
            end = tradeDailyService.getLastData(tsCode);
        }
        return (end.getClose() - start.getOpen()) / start.getOpen();

    }

    public List<String> calSuperTrend(List<SuperTrendTradeSignal> data) {
        Map<String, List<SuperTrendTradeSignal>> resultMap = data.stream().collect(Collectors.groupingBy(SuperTrendTradeSignal::getTsCode));
        List<String> calResult = new ArrayList<>();
        double totalYield = 0.0;
        for (Map.Entry<String, List<SuperTrendTradeSignal>> entry : resultMap.entrySet()) {
            String tsCode = entry.getKey();
            if (!filterStock(tsCode)) {
                log.info("该股票不参与计算, tsCode:{}", tsCode);
                continue;
            }
            List<SuperTrendTradeSignal> superTrendTradeSignalList = entry.getValue();
            double yieldRate = 0.0;
            SuperTrendTradeSignal buySignal = null;
            for (int i = 0; i < superTrendTradeSignalList.size(); i++) {
                SuperTrendTradeSignal temp = superTrendTradeSignalList.get(i);
                if (temp.getAction().equals("BUY")) {
                    buySignal = temp;
                } else if (temp.getAction().equals("SELL")) {
                    if (buySignal != null) {
                        yieldRate = yieldRate + calBackTestByTsCode(tsCode, buySignal.getTradeDate(), temp.getTradeDate());
                        buySignal = null;
                    } else {
                        log.warn("未买入不计算， {}", superTrendTradeSignalList);
                    }
                }
            }
            if (buySignal != null) {
                // 最后一次买入，计算到最新交易日的收益
                yieldRate = yieldRate + calBackTestByTsCode(tsCode, buySignal.getTradeDate(), "latest");
            }
            yieldRate = yieldRate * 100;
            calResult.add(tsCode + ": " + BigDecimal.valueOf(yieldRate).setScale(2, RoundingMode.HALF_UP));
            totalYield = totalYield + yieldRate;
        }
        calResult.add("总收益:" + BigDecimal.valueOf(totalYield).setScale(2, RoundingMode.HALF_UP));
        return calResult;
    }

    /**
     * 过滤股票
     * 北交所的不参与计算
     * st股票不参与计算
     * 科创板不参与计算
     */
    private boolean filterStock(String tsCode) {
        if (tsCode.startsWith("8")) {
            return false;
        }
        String name = iStockBasicService.getStockNameByTsCode(tsCode);
        if (StringUtils.isEmpty(name)) {
            log.warn("redis name is null. tsCode: {}", tsCode);
        }
        if (StringUtils.isNotEmpty(name) && name.startsWith("ST")) {
            return false;
        }
        if (tsCode.startsWith("688")) {
            return false;
        }
        return true;
    }
}
