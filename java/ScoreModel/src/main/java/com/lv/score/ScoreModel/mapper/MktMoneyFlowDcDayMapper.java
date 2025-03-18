package com.lv.score.ScoreModel.mapper;

import com.lv.score.ScoreModel.entity.MktMoneyFlowDcDay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 大盘资金流向（DC） Mapper 接口
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-17
 */
public interface MktMoneyFlowDcDayMapper extends BaseMapper<MktMoneyFlowDcDay> {

    List<MktMoneyFlowDcDay> getLast90DayDate();
}
