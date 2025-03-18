package com.lv.score.ScoreModel.mapper;

import com.lv.score.ScoreModel.entity.StockCyqPerfDay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 每日筹码及胜率 Mapper 接口
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-18
 */
public interface StockCyqPerfDayMapper extends BaseMapper<StockCyqPerfDay> {

    @Select("SELECT MAX(trade_date) from stock_cyq_perf_day")
    String getLatestDay();

}
