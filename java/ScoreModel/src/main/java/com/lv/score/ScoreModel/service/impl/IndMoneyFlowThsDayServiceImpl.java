package com.lv.score.ScoreModel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lv.score.ScoreModel.entity.IndMoneyFlowThsDay;
import com.lv.score.ScoreModel.mapper.IndMoneyFlowThsDayMapper;
import com.lv.score.ScoreModel.service.IIndMoneyFlowThsDayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 板块资金流向(同花顺) 服务实现类
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-17
 */
@Service
public class IndMoneyFlowThsDayServiceImpl extends ServiceImpl<IndMoneyFlowThsDayMapper, IndMoneyFlowThsDay> implements IIndMoneyFlowThsDayService {

    @Override
    public List<IndMoneyFlowThsDay> getListByTradeDate(String tradeDate) {
        QueryWrapper<IndMoneyFlowThsDay> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("trade_date", tradeDate);
        return list(queryWrapper);
    }
}
