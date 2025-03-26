package com.lv.score.ScoreModel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lv.score.ScoreModel.config.RedisInitializer;
import com.lv.score.ScoreModel.entity.StockBasic;
import com.lv.score.ScoreModel.mapper.StockBasicMapper;
import com.lv.score.ScoreModel.service.IStockBasicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

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

    @Override
    public List<StockBasic> getAllData() {
        return list();
    }

    @Override
    public String getStockNameByTsCode(String tsCode) {
        return (String) redisTemplate.opsForHash().get(RedisInitializer.TSCODE_NAME_REDIS_KEY, tsCode);
    }


}
