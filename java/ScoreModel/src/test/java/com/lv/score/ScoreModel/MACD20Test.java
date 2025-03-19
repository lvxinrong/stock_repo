package com.lv.score.ScoreModel;

import com.lv.score.ScoreModel.stock_strategy.macd.MACDStrategyExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MACD20Test {

    @Autowired
    MACDStrategyExecutor executor;

    @Test
    public void testSave() {
        executor.generateMACD20DayReport2ES();
    }
}
