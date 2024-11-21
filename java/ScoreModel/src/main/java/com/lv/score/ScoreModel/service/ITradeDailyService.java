package com.lv.score.ScoreModel.service;

import com.lv.score.ScoreModel.entity.TradeDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-11-15
 */
public interface ITradeDailyService extends IService<TradeDaily> {

    List<TradeDaily> getTradeDailyDataByTsCodeAndTradeDate(String tsCode, String tradeDate);

    List<TradeDaily> getTradeDailyByListDate(String tsCode, List<String> tradeDateList);

}
