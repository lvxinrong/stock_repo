package com.lv.score.ScoreModel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lv.score.ScoreModel.entity.StockStkFactorData;
import com.lv.score.ScoreModel.entity.page_plus.PageDTO;
import com.lv.score.ScoreModel.entity.query.StockSktFactorDataQuery;
import com.lv.score.ScoreModel.mapper.StockStkFactorDataMapper;
import com.lv.score.ScoreModel.service.IStockStkFactorDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 股票技术因子 服务实现类
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-18
 */
@Service
public class StockStkFactorDataServiceImpl extends ServiceImpl<StockStkFactorDataMapper, StockStkFactorData> implements IStockStkFactorDataService {

    @Autowired
    StockStkFactorDataMapper stockStkFactorDataMapper;

    @Override
    public List<StockStkFactorData> getLatestData() {
        QueryWrapper<StockStkFactorData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("trade_date", getLatestDate());
        return list(queryWrapper);
    }

    @Override
    public String getLatestDate() {
        return stockStkFactorDataMapper.getLastDate();
    }

    @Override
    public IPage<StockStkFactorData> queryByPage(StockSktFactorDataQuery stockSktFactorDataQuery) {
        IPage<StockStkFactorData> page = new Page<>(stockSktFactorDataQuery.getPage(), stockSktFactorDataQuery.getSize());
        QueryWrapper<StockStkFactorData> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(stockSktFactorDataQuery.getTradeDate())) {
            queryWrapper.eq("trade_date", stockSktFactorDataQuery.getTradeDate());
        }
        if(StringUtils.isNotEmpty(stockSktFactorDataQuery.getTsCode())) {
            queryWrapper.eq("ts_code", stockSktFactorDataQuery.getTsCode());
        }
        stockStkFactorDataMapper.selectPage(page,queryWrapper);
        return page;
    }

    @Override
    public List<StockStkFactorData> getLast5DaysData() {
        return stockStkFactorDataMapper.getLast5DaysData();
    }

    @Override
    public List<StockStkFactorData> getLast20DaysData() {
        return stockStkFactorDataMapper.getLast20DaysData();
    }
}
