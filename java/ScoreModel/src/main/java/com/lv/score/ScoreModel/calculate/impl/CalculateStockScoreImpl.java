package com.lv.score.ScoreModel.calculate.impl;

import com.lv.score.ScoreModel.calculate.CalculateStockScore;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultDaily;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultMonth;
import com.lv.score.ScoreModel.calculate.entity.StockScoreInputParam;
import com.lv.score.ScoreModel.calculate.util.TsCodeIndexConstant;
import com.lv.score.ScoreModel.entity.IndexBasicDaily;
import com.lv.score.ScoreModel.entity.TradeDaily;
import com.lv.score.ScoreModel.service.IIndexBasicDailyService;
import com.lv.score.ScoreModel.service.IStockBasicService;
import com.lv.score.ScoreModel.service.ITradeDailyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class CalculateStockScoreImpl implements CalculateStockScore {

    @Autowired
    ITradeDailyService iTradeDailyService;

    @Autowired
    IIndexBasicDailyService iIndexBasicDailyService;

    @Autowired
    IStockBasicService iStockBasicService;

    @Override
    public List<CalculateResultDaily> getStockScore(StockScoreInputParam stockScoreInputParam) {
        List<CalculateResultDaily> result = new ArrayList<>();
        String tsCode = stockScoreInputParam.getTs_code();
        // 查询当前ts code 对应时间的所有交易数据
        List<TradeDaily> tradeDailies = iTradeDailyService.getTradeDailyDataByTsCodeAndTradeDate(stockScoreInputParam.getTs_code(), stockScoreInputParam.getDate());
        // 查询当前股票所对应板块的指数数据
        String index_ts_code = TsCodeIndexConstant.getIndexTsCode(stockScoreInputParam.getTs_code());

        List<IndexBasicDaily> indexBasicDailies = iIndexBasicDailyService.getTradeDailyDataByTsCodeAndTradeDate(index_ts_code, stockScoreInputParam.getDate());

        for (TradeDaily tradeDaily : tradeDailies) {
            String tradeDate = tradeDaily.getTradeDate();
            Optional<IndexBasicDaily> curr = indexBasicDailies.stream().filter(x -> Objects.equals(x.getTradeDate(), tradeDate)).findAny();
            if (curr.isEmpty()) {
                log.info("发现为空的交易数据 tradeDaily: {}", tradeDaily);
                continue;
            }
            CalculateResultDaily calculateResultDaily = calculateResultDaily(tradeDaily, curr.get());
            result.add(calculateResultDaily);
        }
        return result;
    }

    private CalculateResultDaily calculateResultDaily(TradeDaily tradeDaily, IndexBasicDaily indexBasicDaily) {
        CalculateResultDaily calculateResultDaily = new CalculateResultDaily();
        calculateResultDaily.setTs_code(tradeDaily.getTsCode());
        calculateResultDaily.setIndex_ts_code(indexBasicDaily.getTsCode());
        calculateResultDaily.setStock_pct_chg(tradeDaily.getPctChg());
        calculateResultDaily.setIndex_pct_chg(indexBasicDaily.getPctChg());
        calculateResultDaily.setTradeDate(tradeDaily.getTradeDate());
        Double score = tradeDaily.getPctChg() - indexBasicDaily.getPctChg();

        calculateResultDaily.setScore(score);

        return calculateResultDaily;
    }



    @Override
    public CalculateResultMonth getStockScoreMonth(List<CalculateResultDaily> calculateResultDailyList) {
        CalculateResultMonth calculateResultMonth = new CalculateResultMonth();
        calculateResultMonth.setTradeDateMonth(calculateResultDailyList.get(0).getTradeDate().substring(0, 6));
        calculateResultMonth.setTs_code(calculateResultDailyList.get(0).getTs_code());
        calculateResultMonth.setStock_name(getStockName(calculateResultDailyList.get(0).getTs_code()));
        calculateResultMonth.setScore(calculateResultDailyList.stream().mapToDouble(CalculateResultDaily::getScore).sum());
        return calculateResultMonth;
    }

    private String getStockName(String tsCode) {
        return iStockBasicService.queryByTsCode(tsCode).getName();
    }

}
