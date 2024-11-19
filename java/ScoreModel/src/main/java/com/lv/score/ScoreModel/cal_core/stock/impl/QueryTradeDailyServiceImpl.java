package com.lv.score.ScoreModel.cal_core.stock.impl;

import com.lv.score.ScoreModel.cal_core.stock.QueryTradeDailyService;
import com.lv.score.ScoreModel.entity.TradeDaily;
import com.lv.score.ScoreModel.service.ITradeDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueryTradeDailyServiceImpl implements QueryTradeDailyService {

    @Autowired
    ITradeDailyService iTradeDailyService;

    @Override
    public List<TradeDaily> getTradeDaily(String tsCode, String tradeMonth) {
        // 查询当前ts code 对应时间的所有交易数据
        return iTradeDailyService.getTradeDailyDataByTsCodeAndTradeDate(tsCode, tradeMonth);
    }
}
