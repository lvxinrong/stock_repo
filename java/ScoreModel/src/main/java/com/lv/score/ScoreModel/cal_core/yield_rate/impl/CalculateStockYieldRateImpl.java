package com.lv.score.ScoreModel.cal_core.yield_rate.impl;

import com.lv.score.ScoreModel.cal_core.yield_rate.CalculateStockYieldRate;
import com.lv.score.ScoreModel.entity.TradeDaily;
import com.lv.score.ScoreModel.service.ITradeDailyService;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Slf4j
public class CalculateStockYieldRateImpl implements CalculateStockYieldRate {

    @Autowired
    ITradeDailyService iTradeDailyService;

    @Override
    public Double getStockYieldRateWithStartTime(String tsCode, String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<TradeDaily> dailyList = iTradeDailyService.getTradeDailyByListDate(tsCode,
                StringUtils.isBlank(endTime) ? Collections.singletonList(startTime) : Arrays.asList(startTime, endTime));
        Optional<TradeDaily> startTradeDaily = dailyList.stream().min(
                Comparator.comparing(e -> LocalDate.parse(e.getTradeDate(), formatter)));
        Optional<TradeDaily> endTradeDaily = dailyList.stream().max(
                Comparator.comparing(e -> LocalDate.parse(e.getTradeDate(), formatter)));

        if (startTradeDaily.isEmpty() || endTradeDaily.isEmpty()) {
            log.error("getStockYieldRateWithStartTime 数据有误。 tsCode: {}, startTime: {}, endTime: {}", tsCode, startTime, endTime);
            return -99999999999d;
        }
        Double open = startTradeDaily.get().getPreClose();
        Double end = endTradeDaily.get().getClose();
        return (end - open) / open * 100;
    }
}
