package com.lv.score.ScoreModel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.lv.score.ScoreModel.entity.LimitUpCountEntity;
import com.lv.score.ScoreModel.entity.LimitUpThsDaily;
import com.lv.score.ScoreModel.mapper.LimitUpThsDailyMapper;
import com.lv.score.ScoreModel.service.ILimitUpThsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
public class LimitUpThsDailyServiceImpl extends ServiceImpl<LimitUpThsDailyMapper, LimitUpThsDaily> implements ILimitUpThsDailyService {

    @Override
    public List<LimitUpThsDaily> getCurrentDate() {
        QueryWrapper<LimitUpThsDaily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("trade_date", latestDay());
        return list(queryWrapper);
    }

    @Override
    public String latestDay() {
        return this.baseMapper.getLatestDay();
    }

    @Override
    public List<LimitUpCountEntity> getLast60UpDate() {
        return baseMapper.getLast60UpDate();
    }


}
