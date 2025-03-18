package com.lv.score.ScoreModel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lv.score.ScoreModel.entity.LimitDownThsDaily;
import com.lv.score.ScoreModel.entity.LimitUpThsDaily;
import com.lv.score.ScoreModel.mapper.LimitDownThsDailyMapper;
import com.lv.score.ScoreModel.service.ILimitDownThsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-13
 */
@Service
public class LimitDownThsDailyServiceImpl extends ServiceImpl<LimitDownThsDailyMapper, LimitDownThsDaily> implements ILimitDownThsDailyService {

    @Override
    public List<LimitDownThsDaily> getCurrentDate() {
        QueryWrapper<LimitDownThsDaily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("trade_date", latestDay());
        return list(queryWrapper);
    }

    @Override
    public String latestDay() {
        return this.baseMapper.getLatestDay();
    }
}
