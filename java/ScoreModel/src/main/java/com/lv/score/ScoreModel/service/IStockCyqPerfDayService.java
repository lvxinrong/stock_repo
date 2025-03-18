package com.lv.score.ScoreModel.service;

import com.lv.score.ScoreModel.entity.StockCyqPerfDay;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 每日筹码及胜率 服务类
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-18
 */
public interface IStockCyqPerfDayService extends IService<StockCyqPerfDay> {

    List<StockCyqPerfDay> getLatestDateList();

    String latestDay();
}
