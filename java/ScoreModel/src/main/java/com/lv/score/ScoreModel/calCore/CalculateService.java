package com.lv.score.ScoreModel.calCore;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.lv.score.ScoreModel.calCore.CalculateServiceInterface;
import com.lv.score.ScoreModel.calCore.cal.CalCoreService;
import com.lv.score.ScoreModel.calCore.entity.IndexStock;
import com.lv.score.ScoreModel.calCore.index.QueryIndexStockFactoryService;
import com.lv.score.ScoreModel.calCore.save.SaveCalResult;
import com.lv.score.ScoreModel.calCore.stock.QueryTradeDailyService;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultDaily;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultMonth;
import com.lv.score.ScoreModel.entity.IndexBasicDaily;
import com.lv.score.ScoreModel.entity.TradeDaily;
import com.lv.score.ScoreModel.service.IIndexBasicDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CalculateService implements CalculateServiceInterface {

    @Autowired
    QueryIndexStockFactoryService queryIndexStockFactoryService;

    @Autowired
    IIndexBasicDailyService iIndexBasicDailyService;

    @Autowired
    QueryTradeDailyService queryTradeDailyService;

    @Autowired
    CalCoreService calCoreService;

    @Autowired
    SaveCalResult saveCalResult;

    @Value("${stock.stock_month}")
    private String stock_month;

    @Override
    public void startCalculateStockTradeDailyScore(String indexCode) {
        /**
         * 1. 读取指数成分股
         * 2. 读取指数每日交易数据
         * 3. 读取个股每日交易数据
         * 4. 根据指数成分股计算 当日涨跌幅和指数的偏差指
         * 5. 组装结果数据，保存
         */
        Map<String, List<CalculateResultDaily>> calResultMap = new HashMap<>();
        List<CalculateResultMonth> monthScoreList = new ArrayList<>();
        List<IndexStock> indexStockList = queryIndexStockFactoryService.getIndexStock(indexCode);
        List<IndexBasicDaily> indexBasicDailies = iIndexBasicDailyService.getTradeDailyDataByTsCodeAndTradeDate(indexCode, stock_month);
        Map<String, IndexBasicDaily> indexBasicDailyMap = indexBasicDailies.stream().collect(Collectors.toMap(IndexBasicDaily::getTradeDate, Function.identity()));
        for (IndexStock i :indexStockList) {
            List<TradeDaily> tradeDailyList = queryTradeDailyService.getTradeDaily(i.getConCode(), stock_month);
            List<CalculateResultDaily> dailies = calCoreService.calStockScoreList(indexBasicDailyMap, tradeDailyList);
            calResultMap.put(i.getConCode() + i.getTradeDate(), dailies);
            monthScoreList.add(calCoreService.getMonthScore(dailies));
        }
        saveCalResult.saveDailyScoreToES(calResultMap);
        saveCalResult.saveMonthScoreToES(monthScoreList);
    }
}
