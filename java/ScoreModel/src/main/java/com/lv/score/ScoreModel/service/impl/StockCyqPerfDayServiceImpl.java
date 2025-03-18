package com.lv.score.ScoreModel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lv.score.ScoreModel.entity.StockCyqPerfDay;
import com.lv.score.ScoreModel.mapper.StockCyqPerfDayMapper;
import com.lv.score.ScoreModel.service.IStockCyqPerfDayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
