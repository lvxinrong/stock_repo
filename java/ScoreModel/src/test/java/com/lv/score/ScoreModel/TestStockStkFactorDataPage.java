package com.lv.score.ScoreModel;

import com.lv.score.ScoreModel.entity.query.StockSktFactorDataQuery;
import com.lv.score.ScoreModel.service.IStockStkFactorDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
