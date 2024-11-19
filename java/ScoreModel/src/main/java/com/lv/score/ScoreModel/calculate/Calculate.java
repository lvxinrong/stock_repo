package com.lv.score.ScoreModel.calculate;

import com.lv.score.ScoreModel.calculate.entity.CalculateResultDaily;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultMonth;
import com.lv.score.ScoreModel.calculate.entity.StockScoreInputParam;
import com.lv.score.ScoreModel.entity.StockBasic;
import com.lv.score.ScoreModel.service.IStockBasicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class Calculate {

    @Autowired
    CalculateStockScore calculateStockScore;

    @Autowired
    IStockBasicService iStockBasicService;

    @Value("${stock.stock_month}")
    private String stock_month;

    public List<CalculateResultMonth> getMonthScore() {
        List<CalculateResultMonth> resultMonths = new ArrayList<>();
        List<StockBasic> stockBasics = iStockBasicService.getStockBasicByMarket("创业板");
        for (StockBasic stockBasic : stockBasics) {
            StockScoreInputParam stockScoreInputParam = new StockScoreInputParam();
            stockScoreInputParam.setTs_code(stockBasic.getTsCode());
            stockScoreInputParam.setDate(stock_month);
            List<CalculateResultDaily> calculateResultDailyList = calculateStockScore.getStockScore(stockScoreInputParam);
            if (calculateResultDailyList.size() == 0) {
                log.warn("calculateResultDailyList size is zero, StockBasic: {}", stockBasic);
                continue;
            }

//            //TODO 测试使用
//            if (resultMonths.size() > 10) {
//                break;
//            }
            CalculateResultMonth value = calculateStockScore.getStockScoreMonth(calculateResultDailyList);
            resultMonths.add(value);
        }
        return resultMonths;
    }
}
