package com.lv.score.ScoreModel;

import com.lv.score.ScoreModel.calculate.CalculateStockScore;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultDaily;
import com.lv.score.ScoreModel.calculate.entity.StockScoreInputParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CalculateStockScoreTest {

    @Autowired
    CalculateStockScore calculateStockScore;

    @Test
    public void test() {
        StockScoreInputParam stockScoreInputParam = new StockScoreInputParam();
        stockScoreInputParam.setDate("202410");
        stockScoreInputParam.setTs_code("301000.SZ");

        List<CalculateResultDaily> result = calculateStockScore.getStockScore(stockScoreInputParam);
        System.out.println(result);
        System.out.println(result.stream().mapToDouble(CalculateResultDaily::getScore).sum());
    }
}
