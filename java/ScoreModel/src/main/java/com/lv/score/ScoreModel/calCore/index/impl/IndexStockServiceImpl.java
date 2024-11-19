package com.lv.score.ScoreModel.calCore.index.impl;

import com.lv.score.ScoreModel.calCore.entity.IndexStock;
import com.lv.score.ScoreModel.calCore.index.IndexStockService;
import com.lv.score.ScoreModel.calCore.index.QueryIndexStockService;
import com.lv.score.ScoreModel.entity.IndexBasicDaily;
import com.lv.score.ScoreModel.service.IIndexBasicDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IndexStockServiceImpl implements IndexStockService {

    @Autowired
    IIndexBasicDailyService iIndexBasicDailyService;

    @Value("${stock.stock_month}")
    private String stock_month;

    @Override
    public List<IndexBasicDaily> getIndexTradeDaily(String indexCode) {
        return iIndexBasicDailyService.getTradeDailyDataByTsCodeAndTradeDate(indexCode, stock_month);
    }
}
