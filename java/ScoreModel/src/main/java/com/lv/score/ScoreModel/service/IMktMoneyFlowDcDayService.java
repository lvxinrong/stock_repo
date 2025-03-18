package com.lv.score.ScoreModel.service;

import com.lv.score.ScoreModel.entity.MktMoneyFlowDcDay;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 大盘资金流向（DC） 服务类
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-17
 */
public interface IMktMoneyFlowDcDayService extends IService<MktMoneyFlowDcDay> {

    List<MktMoneyFlowDcDay> getLast90DayDate();
}
