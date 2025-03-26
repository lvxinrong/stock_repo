package com.lv.score.ScoreModel.schedul;

import com.lv.score.ScoreModel.stock_strategy.stock_cyq.StockCyqPerfExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UpdateStockCyqJob {

    @Autowired
    StockCyqPerfExecutor stockCyqPerfExecutor;

    @Scheduled(cron = "0 0 20 * * ?")
    public void executeSaveLast20DataToEs() {
        stockCyqPerfExecutor.saveLast20DataToEs();
    }

}
