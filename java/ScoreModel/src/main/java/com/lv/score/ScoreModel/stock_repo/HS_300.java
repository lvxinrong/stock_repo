package com.lv.score.ScoreModel.stock_repo;

import com.lv.score.ScoreModel.entity.Hs300Stock;
import com.lv.score.ScoreModel.service.IHs300StockService;
import com.lv.score.ScoreModel.service.impl.Hs300StockServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HS_300 {

    @Autowired
    IHs300StockService iHs300StockService;

    public List<Hs300Stock> getHs300StockList(String tradeDate) {
        if (StringUtils.isBlank(tradeDate)) {
            tradeDate = getAvailableLastDay();
        }
        return iHs300StockService.getTradeDateStockList(tradeDate);
    }

    /**
     * 获取HS_300表中最新的可用交易
     * @return
     */
    private String getAvailableLastDay() {
        return iHs300StockService.getLastTradeDate();
    }
}
