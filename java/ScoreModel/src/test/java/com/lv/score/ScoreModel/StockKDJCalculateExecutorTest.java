package com.lv.score.ScoreModel;

import com.lv.score.ScoreModel.stock_strategy.kdj.StockKDJCalculateExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StockKDJCalculateExecutorTest {

    @Autowired
    StockKDJCalculateExecutor stockKDJCalculateExecutor;

    @Test
    public void gen14Result() {
        stockKDJCalculateExecutor.generate14PeriodKDJResult();
    }
}
