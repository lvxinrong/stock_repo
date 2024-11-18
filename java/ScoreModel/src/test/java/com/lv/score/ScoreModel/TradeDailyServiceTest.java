package com.lv.score.ScoreModel;

import com.lv.score.ScoreModel.calculate.Calculate;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultMonth;
import com.lv.score.ScoreModel.calculate.save.CalculateResultMonthEsEntity;
import com.lv.score.ScoreModel.calculate.save.SaveMontScoreToEs;
import com.lv.score.ScoreModel.service.ITradeDailyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class TradeDailyServiceTest {

    @Autowired
    ITradeDailyService iTradeDailyService;

    @Autowired
    Calculate calculate;

    @Autowired
    SaveMontScoreToEs saveMontScoreToEs;

    @Test
    public void test() {
        System.out.println(iTradeDailyService.getTradeDailyDataByTsCodeAndTradeDate("300350.SZ", "202410"));
    }

    @Test
    public void testScore() {
        System.out.println(calculate.getMonthScore());
    }

    @Test
    public void testSaveToEs() throws IOException {
        List<CalculateResultMonth> resultMonths = calculate.getMonthScore();
        List<CalculateResultMonthEsEntity> saveList = resultMonths.stream().map(CalculateResultMonthEsEntity::new).toList();
        System.out.println(saveList);
        saveMontScoreToEs.bulkSave(saveList);
    }
}
