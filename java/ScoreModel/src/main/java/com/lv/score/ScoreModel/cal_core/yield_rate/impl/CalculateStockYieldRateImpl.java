package com.lv.score.ScoreModel.cal_core.yield_rate.impl;

import com.lv.score.ScoreModel.cal_core.yield_rate.CalculateStockYieldRate;
import com.lv.score.ScoreModel.entity.TradeDaily;
import com.lv.score.ScoreModel.service.ITradeDailyService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class CalculateStockYieldRateImpl implements CalculateStockYieldRate {

    @Autowired
    ITradeDailyService iTradeDailyService;

    @Override
    public Double getStockYieldRateWithStartTime(String tsCode, String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<TradeDaily> dailyList = iTradeDailyService.getTradeDailyByListDate(tsCode,
                StringUtils.isBlank(endTime) ? Collections.singletonList(startTime) : Arrays.asList(startTime, endTime));
        TradeDaily startTradeDaily = dailyList.stream().filter(e -> e.getTradeDate().equals(startTime)).findAny().get();
        TradeDaily endTradeDaily = dailyList.stream().max(
                Comparator.comparing(e -> LocalDate.parse(e.getTradeDate(), formatter))
        ).get();
        Double open = startTradeDaily.getOpen();
        Double end = endTradeDaily.getClose();
        return (end - open) / open * 100;
    }
}
