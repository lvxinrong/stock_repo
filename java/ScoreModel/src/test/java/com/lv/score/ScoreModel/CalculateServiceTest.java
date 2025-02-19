package com.lv.score.ScoreModel;

import com.lv.score.ScoreModel.cal_core.CalculateService;
import com.lv.score.ScoreModel.constant.IndexCodeConstant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

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

    @Test
    public void generateAll() {
        List<Future<?>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(5); // 创建一个大小为5的线程池
        futures.add(executorService.submit(() -> calculateService.startCalculateStockTradeDailyScore(IndexCodeConstant.HS_300)));
        futures.add(executorService.submit(() -> calculateService.startCalculateStockTradeDailyScore(IndexCodeConstant.ZH_100)));
        futures.add(executorService.submit(() -> calculateService.startCalculateStockTradeDailyScore(IndexCodeConstant.ZH_500)));
        futures.add(executorService.submit(() -> calculateService.startCalculateStockTradeDailyScore(IndexCodeConstant.ZH_800)));
        futures.add(executorService.submit(() -> calculateService.startCalculateStockTradeDailyScore(IndexCodeConstant.ZH_1000)));
        for (Future<?> future : futures) {
            try {
                future.get(); // 等待任务执行完毕
            } catch (InterruptedException | ExecutionException e) {
                // 处理异常
            }
        }
        System.out.println("finish");
    }
}
