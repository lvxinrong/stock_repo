package com.lv.score.ScoreModel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lv.score.ScoreModel.entity.LimitUpDeepseekAnalyzeResult;
import com.lv.score.ScoreModel.mapper.LimitUpDeepseekAnalyzeResultMapper;
import com.lv.score.ScoreModel.service.ILimitUpDeepseekAnalyzeResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-14
 */
@Service
public class LimitUpDeepseekAnalyzeResultServiceImpl extends ServiceImpl<LimitUpDeepseekAnalyzeResultMapper, LimitUpDeepseekAnalyzeResult> implements ILimitUpDeepseekAnalyzeResultService {

    @Override
    public List<LimitUpDeepseekAnalyzeResult> getLatestDate() {
        QueryWrapper<LimitUpDeepseekAnalyzeResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("trade_date", getLatestTradeDate());
        return list(queryWrapper);
    }

    public String getLatestTradeDate() {
        return baseMapper.getLatestDate();
    }
}
