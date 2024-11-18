package com.lv.score.ScoreModel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lv.score.ScoreModel.entity.StockBasic;
import com.lv.score.ScoreModel.mapper.StockBasicMapper;
import com.lv.score.ScoreModel.service.IStockBasicService;
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
public class StockBasicServiceImpl extends ServiceImpl<StockBasicMapper, StockBasic> implements IStockBasicService {

    @Override
    public StockBasic queryByTsCode(String tsCode) {
        QueryWrapper<StockBasic> wrapper = new QueryWrapper<>();
        wrapper.eq("ts_code", tsCode);
        return getOne(wrapper);
    }

    @Override
    public List<StockBasic> getStockBasicByMarket(String market) {
        QueryWrapper<StockBasic> wrapper = new QueryWrapper<>();
        wrapper.eq("market", market);
        return list(wrapper);
    }
}
