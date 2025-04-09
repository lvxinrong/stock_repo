package com.lv.score.ScoreModel;

import com.lv.score.ScoreModel.entity.StockStkFactorData;
import com.lv.score.ScoreModel.entity.query.StockSktFactorDataQuery;
import com.lv.score.ScoreModel.service.IStockStkFactorDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class TestStockStkFactorDataPage {

    @Autowired
    IStockStkFactorDataService iStockStkFactorDataService;

    @Test
    public void testPage() {
        StockSktFactorDataQuery stockSktFactorDataQuery = new StockSktFactorDataQuery();
        stockSktFactorDataQuery.setPage(1);
        stockSktFactorDataQuery.setSize(100);
        stockSktFactorDataQuery.setTsCode("000001.SZ");
        log.info(iStockStkFactorDataService.queryByPage(stockSktFactorDataQuery).toString());
    }

    @Test
    public void testSelect() {
        List<StockStkFactorData> queryResult = iStockStkFactorDataService.get20DaysData("20250301");
        System.out.println(queryResult.size());
        System.out.println(queryResult.get(0).getTradeDate());
        System.out.println(queryResult.get(queryResult.size() - 1).getTradeDate());
    }
}
