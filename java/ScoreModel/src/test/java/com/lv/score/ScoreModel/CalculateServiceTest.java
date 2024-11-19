package com.lv.score.ScoreModel;

import com.lv.score.ScoreModel.cal_core.CalculateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CalculateServiceTest {

    @Autowired
    CalculateService calculateService;

    @Test
    public void testHs300Score() {
        String index_code = "399300.SZ";
        calculateService.startCalculateStockTradeDailyScore(index_code);
    }
}
