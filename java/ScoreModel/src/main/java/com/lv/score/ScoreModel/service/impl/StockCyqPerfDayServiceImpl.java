package com.lv.score.ScoreModel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lv.score.ScoreModel.entity.StockCyqPerfDay;
import com.lv.score.ScoreModel.entity.StockStkFactorData;
import com.lv.score.ScoreModel.entity.query.StockCyqPerfDayQuery;
import com.lv.score.ScoreModel.mapper.StockCyqPerfDayMapper;
import com.lv.score.ScoreModel.service.IStockCyqPerfDayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 每日筹码及胜率 服务实现类
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-18
 */
@Service
public class StockCyqPerfDayServiceImpl extends ServiceImpl<StockCyqPerfDayMapper, StockCyqPerfDay> implements IStockCyqPerfDayService {

    @Autowired
    StockCyqPerfDayMapper stockCyqPerfDayMapper;

    @Override
    public List<StockCyqPerfDay> getLatestDateList() {
        QueryWrapper<StockCyqPerfDay> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("trade_date", latestDay());
        return list(queryWrapper);
    }

    @Override
    public String latestDay() {
        return this.baseMapper.getLatestDay();
    }

    @Override
    public List<StockCyqPerfDay> getTradeDateList(String tradeDate) {
        QueryWrapper<StockCyqPerfDay> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("trade_date", tradeDate);
        return list(queryWrapper);
    }

    @Override
    public IPage<StockCyqPerfDay> getDataByPage(StockCyqPerfDayQuery stockCyqPerfDayQuery) {
        IPage<StockCyqPerfDay> page = new Page<>(stockCyqPerfDayQuery.getPage(), stockCyqPerfDayQuery.getSize());
        QueryWrapper<StockCyqPerfDay> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(stockCyqPerfDayQuery.getTradeDate())) {
            queryWrapper.eq("trade_date", stockCyqPerfDayQuery.getTradeDate());
        }
        if(StringUtils.isNotEmpty(stockCyqPerfDayQuery.getTsCode())) {
            queryWrapper.eq("ts_code", stockCyqPerfDayQuery.getTsCode());
        }
        stockCyqPerfDayMapper.selectPage(page, queryWrapper);
        return page;
    }

    @Override
    public List<StockCyqPerfDay> getLatest5DaysData() {
        return stockCyqPerfDayMapper.getLatest5DaysData();
    }

    @Override
    public List<StockCyqPerfDay> getLatest20DaysData() {
        return stockCyqPerfDayMapper.getLatest20DaysData();
    }

}
