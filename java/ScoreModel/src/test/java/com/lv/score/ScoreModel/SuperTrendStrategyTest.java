package com.lv.score.ScoreModel;

import com.lv.score.ScoreModel.backtest.BackTestService;
import com.lv.score.ScoreModel.entity.TradeDaily;
import com.lv.score.ScoreModel.service.ITradeDailyService;
import com.lv.score.ScoreModel.stock_strategy.super_trend.OptimizedSuperTrendStrategy;
import com.lv.score.ScoreModel.stock_strategy.super_trend.SuperTrendStrategy;
import com.lv.score.ScoreModel.stock_strategy.super_trend.entity.SuperTrendTradeSignal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
public class SuperTrendStrategyTest {

    @Autowired
    SuperTrendStrategy superTrendStrategy;

    @Autowired
    ITradeDailyService iTradeDailyService;

    @Autowired
    OptimizedSuperTrendStrategy optimizedSuperTrendStrategy;

    @Autowired
    BackTestService backTestService;

    @Test
    public void test() {
        List<TradeDaily> data = iTradeDailyService.getLastData(90);
        List<SuperTrendTradeSignal> result = superTrendStrategy.analyze(data);
        Map<String, List<SuperTrendTradeSignal>> resultMap = result.stream().collect(Collectors.groupingBy(SuperTrendTradeSignal::getTsCode));
        for (Map.Entry<String, List<SuperTrendTradeSignal>> entry : resultMap.entrySet()) {
            entry.getValue().sort(Comparator.comparing(SuperTrendTradeSignal::getTradeDate).reversed());
            System.out.println(entry.getKey() + "趋势结果: " + entry.getValue().toString());
        }
    }

    @Test
    public void testOptimize() {

    }

    @Test
    public void testSuperTrend() {
        List<TradeDaily> data = iTradeDailyService.getLastData(90);
        List<SuperTrendTradeSignal> result = superTrendStrategy.analyze(data);
        List<String> backResult = backTestService.calSuperTrend(result);
        for (String s : backResult) {
            System.out.println(s);
        }
    }

    @Test
    public void testOptimizeBack() {
        List<TradeDaily> data = iTradeDailyService.getLastData(60);
        optimizedSuperTrendStrategy.setRequiredDays(60);
        List<SuperTrendTradeSignal> result = optimizedSuperTrendStrategy.analyze(data);
        List<String> backResult = backTestService.calSuperTrend(result);
        String fileName = "H:\\temp_data\\optimizeBack_period_60.txt";
        writeFileToLocal(backResult, fileName);
    }

    @Test
    public void testOptimizeBackMultiRatio() {
        /**
         * 需要改成根据股票粒度来计算, period = 200 就意味着一次要读120万条数据。
         * 改成批量计算，比如100个股票作为一批 那么就是一次性读取2万条数据进行处理
         * 每批次计算量 = 股票数量 *  period (两个都可以调整，但总量保持不变)
         *
         * 参数需要多维度调整计算
         * */

    }

    private void writeFileToLocal(List<String> wData, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : wData) {
                writer.write(line);
                writer.newLine();  // 确保每行独立
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
