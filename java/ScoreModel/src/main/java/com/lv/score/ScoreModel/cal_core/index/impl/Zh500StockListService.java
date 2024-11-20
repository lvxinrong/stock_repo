package com.lv.score.ScoreModel.cal_core.index.impl;

import com.lv.score.ScoreModel.cal_core.entity.IndexStock;
import com.lv.score.ScoreModel.cal_core.index.QueryIndexStockService;
import com.lv.score.ScoreModel.constant.IndexCodeConstant;
import com.lv.score.ScoreModel.entity.Zh100Stock;
import com.lv.score.ScoreModel.entity.Zh500Stock;
import com.lv.score.ScoreModel.service.IZh100StockService;
import com.lv.score.ScoreModel.service.IZh500StockService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = IndexCodeConstant.ZH_500)
public class Zh500StockListService implements QueryIndexStockService {

    @Value("${stock.stock_month}")
    private String tradeMonth;

    @Autowired
    IZh500StockService iZh500StockService;
    @Override
    public List<IndexStock> getIndexStockList() {
        List<Zh500Stock> result = iZh500StockService.getTradeDateStockList(tradeMonth);
        List<IndexStock> indexStockList = new ArrayList<>();
        for (Zh500Stock stock : result) {
            IndexStock indexStock = new IndexStock();
            BeanUtils.copyProperties(stock,indexStock);
            indexStockList.add(indexStock);
        }
        return indexStockList;
    }
}
