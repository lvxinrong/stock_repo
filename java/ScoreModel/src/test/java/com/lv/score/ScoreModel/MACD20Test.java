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

    @Test
    public void generateHistory() {
        executor.generateMACD20DaysHistory("20250306");
        executor.generateMACD20DaysHistory("20250307");
        executor.generateMACD20DaysHistory("20250310");
        executor.generateMACD20DaysHistory("20250311");
        executor.generateMACD20DaysHistory("20250312");
        executor.generateMACD20DaysHistory("20250313");
        executor.generateMACD20DaysHistory("20250314");
        executor.generateMACD20DaysHistory("20250317");
        executor.generateMACD20DaysHistory("20250318");



    }
}
