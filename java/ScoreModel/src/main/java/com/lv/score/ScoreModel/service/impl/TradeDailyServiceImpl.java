package com.lv.score.ScoreModel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lv.score.ScoreModel.entity.TradeDaily;
import com.lv.score.ScoreModel.mapper.TradeDailyMapper;
import com.lv.score.ScoreModel.service.ITradeDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-11-15
 */
@Service
public class TradeDailyServiceImpl extends ServiceImpl<TradeDailyMapper, TradeDaily> implements ITradeDailyService {

    @Override
    public List<TradeDaily> getTradeDailyDataByTsCodeAndTradeDate(String tsCode, String tradeDate) {
        QueryWrapper<TradeDaily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ts_code", tsCode);
        queryWrapper.like("trade_date", tradeDate + "%");
        return list(queryWrapper);
    }

    @Override
    public List<TradeDaily> getTradeDailyByListDate(String tsCode, List<String> tradeDateList) {
        QueryWrapper<TradeDaily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ts_code", tsCode);
        if (tradeDateList.size() == 1) {
            queryWrapper.ge("trade_date", tradeDateList.get(0));
        } else {
            queryWrapper.in("trade_date", tradeDateList);
        }
        return list(queryWrapper);
    }


}
