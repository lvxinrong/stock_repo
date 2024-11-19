package com.lv.score.ScoreModel.cal_core.index.impl;

import com.lv.score.ScoreModel.cal_core.entity.IndexStock;
import com.lv.score.ScoreModel.cal_core.index.QueryIndexStockService;
import com.lv.score.ScoreModel.entity.Hs300Stock;
import com.lv.score.ScoreModel.service.IHs300StockService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "399300.SZ")
public class HS300StockListServiceImpl implements QueryIndexStockService {

    @Value("${stock.stock_month}")
    private String tradeMonth;

    @Autowired
    IHs300StockService iHs300StockService;

    @Override
    public List<IndexStock> getIndexStockList() {
        List<Hs300Stock> result = iHs300StockService.getTradeDateStockList(tradeMonth);
        List<IndexStock> indexStockList = new ArrayList<>();
        for (Hs300Stock stock : result) {
            IndexStock indexStock = new IndexStock();
            BeanUtils.copyProperties(stock,indexStock);
            indexStockList.add(indexStock);
        }
        return indexStockList;
    }
}
