package com.lv.score.ScoreModel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lv.score.ScoreModel.entity.MktMoneyFlowDcDay;
import com.lv.score.ScoreModel.mapper.MktMoneyFlowDcDayMapper;
import com.lv.score.ScoreModel.service.IMktMoneyFlowDcDayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 大盘资金流向（DC） 服务实现类
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-17
 */
@Service
public class MktMoneyFlowDcDayServiceImpl extends ServiceImpl<MktMoneyFlowDcDayMapper, MktMoneyFlowDcDay> implements IMktMoneyFlowDcDayService {

    @Override
    public List<MktMoneyFlowDcDay> getLast90DayDate() {
        return baseMapper.getLast90DayDate();
    }
}
