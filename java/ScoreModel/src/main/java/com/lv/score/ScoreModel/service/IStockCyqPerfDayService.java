package com.lv.score.ScoreModel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lv.score.ScoreModel.calculate.entity.PageInfo;
import com.lv.score.ScoreModel.entity.StockCyqConcentratedEsVO;
import com.lv.score.ScoreModel.entity.StockCyqPerfDay;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lv.score.ScoreModel.entity.query.StockCyqPerfDayQuery;
import com.lv.score.ScoreModel.stock_strategy.stock_cyq.entity.StockCyqConcentratedEsDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    PageInfo<StockCyqConcentratedEsVO> search(Map<String, Object> conditions, int page, int size) throws IOException;
}
