package com.lv.score.ScoreModel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lv.score.ScoreModel.entity.StockStkFactorData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lv.score.ScoreModel.entity.page_plus.PageDTO;
import com.lv.score.ScoreModel.entity.query.StockSktFactorDataQuery;

import java.util.List;

/**
 * <p>
 * 股票技术因子 服务类
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-18
 */
public interface IStockStkFactorDataService extends IService<StockStkFactorData> {

    List<StockStkFactorData> getLatestData();

    String getLatestDate();

    IPage<StockStkFactorData> queryByPage(StockSktFactorDataQuery stockSktFactorDataQuery);

    List<StockStkFactorData> getLast5DaysData();

    List<StockStkFactorData> getLast20DaysData();

}
