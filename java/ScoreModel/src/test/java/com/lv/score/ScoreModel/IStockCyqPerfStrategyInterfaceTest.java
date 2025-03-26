package com.lv.score.ScoreModel;

import com.lv.score.ScoreModel.stock_strategy.stock_cyq.IStockCyqPerfStrategyInterface;
import com.lv.score.ScoreModel.stock_strategy.stock_cyq.StockCyqPerfExecutor;
import com.lv.score.ScoreModel.stock_strategy.stock_cyq.entity.StockCyqConcentratedResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class IStockCyqPerfStrategyInterfaceTest {

    @Autowired
    IStockCyqPerfStrategyInterface IStockCyqPerfStrategyInterface;

    @Autowired
    StockCyqPerfExecutor stockCyqPerfExecutor;

    @Test
    public void testLatest5DaysStock() {
        List<String> tsCodeList = IStockCyqPerfStrategyInterface.getGoodStockCyqPerfLatest5Days();
        System.out.println(tsCodeList.size());
    }

    @Test
    public void testLatest20DaysStock() {
        List<String> tsCodeList = IStockCyqPerfStrategyInterface.getGoodStockCyqPerfLatest20Days();
        System.out.println(tsCodeList.size());
    }

    @Test
    public void testLast20() {
        List<StockCyqConcentratedResult> result = IStockCyqPerfStrategyInterface.getLast20DaysStock();
        System.out.println(result.toString());
    }

    @Test
    public void testGenerate() {
        stockCyqPerfExecutor.saveLast20DataToEs();
    }
}
