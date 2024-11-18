package com.lv.score.ScoreModel.service;

import com.lv.score.ScoreModel.entity.IndexBasicDaily;
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
public interface IIndexBasicDailyService extends IService<IndexBasicDaily> {

    List<IndexBasicDaily> getTradeDailyDataByTsCodeAndTradeDate(String tsCode, String tradeDate);
}
