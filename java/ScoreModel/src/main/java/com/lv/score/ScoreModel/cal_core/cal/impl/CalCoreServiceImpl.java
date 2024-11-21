package com.lv.score.ScoreModel.cal_core.cal.impl;

import com.lv.score.ScoreModel.cal_core.cal.CalCoreService;
import com.lv.score.ScoreModel.cal_core.yield_rate.CalculateStockYieldRate;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultDaily;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultMonth;
import com.lv.score.ScoreModel.entity.IndexBasicDaily;
import com.lv.score.ScoreModel.entity.TradeDaily;
import com.lv.score.ScoreModel.service.IIndexBasicService;
import com.lv.score.ScoreModel.service.IStockBasicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Slf4j
public class CalCoreServiceImpl implements CalCoreService {

    @Autowired
    IStockBasicService iStockBasicService;

    @Autowired
    IIndexBasicService iIndexBasicService;

    @Autowired
    CalculateStockYieldRate calculateStockYieldRate;

    @Override
    public CalculateResultDaily calStockScoreWithIndex(IndexBasicDaily indexBasicDaily, TradeDaily tradeDaily) {
        CalculateResultDaily calculateResultDaily = new CalculateResultDaily();
        calculateResultDaily.setTs_code(tradeDaily.getTsCode());
        calculateResultDaily.setIndex_ts_code(indexBasicDaily.getTsCode());
        calculateResultDaily.setStock_pct_chg(tradeDaily.getPctChg());
        calculateResultDaily.setIndex_pct_chg(indexBasicDaily.getPctChg());
        calculateResultDaily.setTradeDate(tradeDaily.getTradeDate());
        // 核心算法，这里可以抽取出去，根据模式来选择，目前先这么写
        Double score = tradeDaily.getPctChg() - indexBasicDaily.getPctChg();
        calculateResultDaily.setScore(score);
        return calculateResultDaily;
    }

    @Override
    public List<CalculateResultDaily> calStockScoreList(Map<String, IndexBasicDaily> indexBasicDailyMap, List<TradeDaily> tradeDailyList) {
        List<CalculateResultDaily> result = new ArrayList<>();
        for(TradeDaily tradeDaily : tradeDailyList) {
            IndexBasicDaily indexBasicDaily = indexBasicDailyMap.get(tradeDaily.getTradeDate());
            result.add(calStockScoreWithIndex(indexBasicDaily, tradeDaily));
        }
        return result;
    }

    @Override
    public CalculateResultMonth getMonthScore(List<CalculateResultDaily> calculateResultDailyList) {
        CalculateResultMonth calculateResultMonth = new CalculateResultMonth();
        calculateResultMonth.setTradeDateMonth(calculateResultDailyList.get(0).getTradeDate().substring(0, 6));
        calculateResultMonth.setTs_code(calculateResultDailyList.get(0).getTs_code());
        calculateResultMonth.setStock_name(getStockName(calculateResultDailyList.get(0).getTs_code()));
        calculateResultMonth.setIndex_code(calculateResultDailyList.get(0).getIndex_ts_code());
        calculateResultMonth.setIndex_code_name(getIndexBasicName(calculateResultDailyList.get(0).getIndex_ts_code()));
        calculateResultMonth.setScore(calculateResultDailyList.stream().mapToDouble(CalculateResultDaily::getScore).sum());
        calculateResultMonth.setYieldRate(getStockYield(calculateResultDailyList));
        return calculateResultMonth;
    }

    private String getStockName(String tsCode) {
        return iStockBasicService.queryByTsCode(tsCode).getName();
    }

    private String getIndexBasicName(String indexCode) {
        return iIndexBasicService.selectIndexNameByCode(indexCode);
    }

    private String getStockYield(List<CalculateResultDaily> calculateResultDailyList) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 使用 Stream API 找到最大日期并计算其下一天
        String nextDate = calculateResultDailyList.stream()
                .map(date -> LocalDate.parse(date.getTradeDate(), formatter)) // 将字符串转换为 LocalDate
                .max(LocalDate::compareTo)                    // 找到最大日期
                .map(max -> max.plusDays(1))                  // 计算下一天
                .map(next -> next.format(formatter))          // 格式化回字符串
                .orElse(null);
        Double value = calculateStockYieldRate.getStockYieldRateWithStartTime(calculateResultDailyList.get(0).getTs_code(), nextDate, null);
        return String.format("%.2f", value) + "%";

    }

}
