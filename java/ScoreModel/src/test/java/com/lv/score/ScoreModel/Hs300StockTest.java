package com.lv.score.ScoreModel;

import com.lv.score.ScoreModel.service.IHs300StockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Hs300StockTest {

    @Autowired
    IHs300StockService iHs300StockService;

    @Test
    public void test() {
        System.out.println(iHs300StockService.getLastTradeDate());
    }

    @Test
    public void testList() {
        System.out.println(iHs300StockService.getTradeDateStockList(iHs300StockService.getLastTradeDate()).size());
    }

    @Test
    public void testNewList() {
        System.out.println(iHs300StockService.getTradeDateStockList("202410").size());
    }
}
