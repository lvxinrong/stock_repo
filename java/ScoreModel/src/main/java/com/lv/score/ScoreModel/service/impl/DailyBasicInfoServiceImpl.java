package com.lv.score.ScoreModel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lv.score.ScoreModel.entity.DailyBasicInfo;
import com.lv.score.ScoreModel.mapper.DailyBasicInfoMapper;
import com.lv.score.ScoreModel.service.IDailyBasicInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 股票交易及财务数据 服务实现类
 * </p>
 *
 * @author lvxinrong
 * @since 2024-11-19
 */
@Service
public class DailyBasicInfoServiceImpl extends ServiceImpl<DailyBasicInfoMapper, DailyBasicInfo> implements IDailyBasicInfoService {

    @Autowired
    DailyBasicInfoMapper dailyBasicInfoMapper;

    @Override
    public DailyBasicInfo getLatestData(String tsCode) {
        return dailyBasicInfoMapper.getLatestDataByTsCode(tsCode);
    }

    @Override
    public DailyBasicInfo getBasicInfoByTsCodeAndTradeDate(String tsCode, String tradeDate) {
        QueryWrapper<DailyBasicInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ts_code", tsCode);
        queryWrapper.eq("trade_date", tradeDate);
        return getOne(queryWrapper);
    }
}
