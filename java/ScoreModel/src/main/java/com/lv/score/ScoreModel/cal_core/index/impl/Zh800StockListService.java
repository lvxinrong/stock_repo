package com.lv.score.ScoreModel.cal_core.index.impl;

import com.lv.score.ScoreModel.cal_core.entity.IndexStock;
import com.lv.score.ScoreModel.cal_core.index.QueryIndexStockService;
import com.lv.score.ScoreModel.constant.IndexCodeConstant;
import com.lv.score.ScoreModel.entity.Zh500Stock;
import com.lv.score.ScoreModel.entity.Zh800Stock;
import com.lv.score.ScoreModel.service.IZh500StockService;
import com.lv.score.ScoreModel.service.IZh800StockService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = IndexCodeConstant.ZH_800)
public class Zh800StockListService implements QueryIndexStockService {

    @Value("${stock.stock_month}")
    private String tradeMonth;

    @Autowired
    IZh800StockService iZh800StockService;
    @Override
    public List<IndexStock> getIndexStockList() {
        List<Zh800Stock> result = iZh800StockService.getTradeDateStockList(tradeMonth);
        List<IndexStock> indexStockList = new ArrayList<>();
        for (Zh800Stock stock : result) {
            IndexStock indexStock = new IndexStock();
            BeanUtils.copyProperties(stock,indexStock);
            indexStockList.add(indexStock);
        }
        return indexStockList;
    }
}
