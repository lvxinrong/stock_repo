package com.lv.score.ScoreModel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lv.score.ScoreModel.entity.IndMoneyFlowDcDay;
import com.lv.score.ScoreModel.mapper.IndMoneyFlowDcDayMapper;
import com.lv.score.ScoreModel.service.IIndMoneyFlowDcDayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 板块资金流向（DC） 服务实现类
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-17
 */
@Service
public class IndMoneyFlowDcDayServiceImpl extends ServiceImpl<IndMoneyFlowDcDayMapper, IndMoneyFlowDcDay> implements IIndMoneyFlowDcDayService {

    @Override
    public List<IndMoneyFlowDcDay> getListByTradeDate(String tradeDate) {
        QueryWrapper<IndMoneyFlowDcDay> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("trade_date", tradeDate);
        return list(queryWrapper);
    }
}
