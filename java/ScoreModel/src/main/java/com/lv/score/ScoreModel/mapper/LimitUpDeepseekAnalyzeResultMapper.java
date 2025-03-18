package com.lv.score.ScoreModel.mapper;

import com.lv.score.ScoreModel.entity.LimitUpDeepseekAnalyzeResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-14
 */
public interface LimitUpDeepseekAnalyzeResultMapper extends BaseMapper<LimitUpDeepseekAnalyzeResult> {

    @Select("SELECT MAX(trade_date) from limit_up_deepseek_analyze_result")
    String getLatestDate();
}
