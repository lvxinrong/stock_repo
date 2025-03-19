package com.lv.score.ScoreModel;

import com.lv.score.ScoreModel.stock_strategy.stock_cyq.StockCyqPerfStrategyInterface;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class StockCyqPerfStrategyInterfaceTest {

    @Autowired
    StockCyqPerfStrategyInterface stockCyqPerfStrategyInterface;

    @Test
    public void testLatest5DaysStock() {
        List<String> tsCodeList = stockCyqPerfStrategyInterface.getGoodStockCyqPerfLatest5Days();
        System.out.println(tsCodeList.size());
    }

    @Test
    public void testLatest20DaysStock() {
        List<String> tsCodeList = stockCyqPerfStrategyInterface.getGoodStockCyqPerfLatest20Days();
        System.out.println(tsCodeList.size());
    }
}
