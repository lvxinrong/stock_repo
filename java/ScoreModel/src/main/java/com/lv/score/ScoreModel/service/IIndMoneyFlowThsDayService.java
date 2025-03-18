package com.lv.score.ScoreModel.service;

import com.lv.score.ScoreModel.entity.IndMoneyFlowThsDay;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 板块资金流向(同花顺) 服务类
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-17
 */
public interface IIndMoneyFlowThsDayService extends IService<IndMoneyFlowThsDay> {

    List<IndMoneyFlowThsDay> getListByTradeDate(String tradeDate);
}
