package com.lv.score.ScoreModel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lv.score.ScoreModel.entity.StockCyqPerfDay;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lv.score.ScoreModel.entity.query.StockCyqPerfDayQuery;

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

    List<StockCyqPerfDay> getTradeDateList(String tradeDate);

    IPage<StockCyqPerfDay> getDataByPage(StockCyqPerfDayQuery stockCyqPerfDayQuery);

    List<StockCyqPerfDay> getLatest5DaysData();

    List<StockCyqPerfDay> getLatest20DaysData();
}
