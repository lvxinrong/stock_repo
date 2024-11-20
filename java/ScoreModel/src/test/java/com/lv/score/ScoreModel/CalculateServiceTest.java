package com.lv.score.ScoreModel;

import com.lv.score.ScoreModel.cal_core.CalculateService;
import com.lv.score.ScoreModel.constant.IndexCodeConstant;
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

    @Test
    public void testZh100Score() {
        String index_code = IndexCodeConstant.ZH_100;
        calculateService.startCalculateStockTradeDailyScore(index_code);
    }

    @Test
    public void testZh500Score() {
        String index_code = IndexCodeConstant.ZH_500;
        calculateService.startCalculateStockTradeDailyScore(index_code);
    }

    @Test
    public void testZh800Score() {
        String index_code = IndexCodeConstant.ZH_800;
        calculateService.startCalculateStockTradeDailyScore(index_code);
    }

    @Test
    public void testZh1000Score() {
        String index_code = IndexCodeConstant.ZH_1000;
        calculateService.startCalculateStockTradeDailyScore(index_code);
    }
}
