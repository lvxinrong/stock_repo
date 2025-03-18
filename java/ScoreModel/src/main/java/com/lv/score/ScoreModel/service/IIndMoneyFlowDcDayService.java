package com.lv.score.ScoreModel.service;

import com.lv.score.ScoreModel.entity.IndMoneyFlowDcDay;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 板块资金流向（DC） 服务类
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-17
 */
public interface IIndMoneyFlowDcDayService extends IService<IndMoneyFlowDcDay> {

    List<IndMoneyFlowDcDay> getListByTradeDate(String tradeDate);

}
