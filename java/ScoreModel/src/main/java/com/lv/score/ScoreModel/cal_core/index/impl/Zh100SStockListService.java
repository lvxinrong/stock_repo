package com.lv.score.ScoreModel.cal_core.index.impl;

import com.lv.score.ScoreModel.cal_core.entity.IndexStock;
import com.lv.score.ScoreModel.cal_core.index.QueryIndexStockService;
import com.lv.score.ScoreModel.constant.IndexCodeConstant;
import com.lv.score.ScoreModel.entity.Hs300Stock;
import com.lv.score.ScoreModel.entity.Zh100Stock;
import com.lv.score.ScoreModel.service.IZh100StockService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service(value = IndexCodeConstant.ZH_100)
public class Zh100SStockListService implements QueryIndexStockService {

    @Value("${stock.stock_month}")
    private String tradeMonth;

    @Autowired
    IZh100StockService iZh100StockService;
    @Override
    public List<IndexStock> getIndexStockList() {
        List<Zh100Stock> result = iZh100StockService.getTradeDateStockList(tradeMonth);
        List<IndexStock> indexStockList = new ArrayList<>();
        for (Zh100Stock stock : result) {
            IndexStock indexStock = new IndexStock();
            BeanUtils.copyProperties(stock,indexStock);
            indexStockList.add(indexStock);
        }
        return indexStockList;
    }

}
