package com.lv.score.ScoreModel.cal_core.stock;

import com.lv.score.ScoreModel.entity.TradeDaily;

import java.util.List;

public interface QueryTradeDailyService {

    List<TradeDaily> getTradeDaily(String tsCode, String tradeMonth);
}
