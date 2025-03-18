package com.lv.score.ScoreModel.mapper;

import com.lv.score.ScoreModel.entity.LimitDownThsDaily;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-13
 */
public interface LimitDownThsDailyMapper extends BaseMapper<LimitDownThsDaily> {

    String getLatestDay();

}
