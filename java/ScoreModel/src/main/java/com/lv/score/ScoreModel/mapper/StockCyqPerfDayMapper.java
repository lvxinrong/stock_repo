package com.lv.score.ScoreModel.mapper;

import com.lv.score.ScoreModel.entity.StockCyqPerfDay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 每日筹码及胜率 Mapper 接口
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-18
 */
@Mapper
public interface StockCyqPerfDayMapper extends BaseMapper<StockCyqPerfDay> {

    @Select("SELECT MAX(trade_date) from stock_cyq_perf_day")
    String getLatestDay();

    @Select("""
            SELECT *
            FROM (
                SELECT
                    *,
                    ROW_NUMBER() OVER (PARTITION BY ts_code ORDER BY trade_date DESC) AS rn
                FROM stock_cyq_perf_day
            ) sub
            WHERE rn <= 5;
            """)
    List<StockCyqPerfDay> getLatest5DaysData();

    @Select("""
            SELECT *
            FROM (
                SELECT
                    *,
                    ROW_NUMBER() OVER (PARTITION BY ts_code ORDER BY trade_date DESC) AS rn
                FROM stock_cyq_perf_day
            ) sub
            WHERE rn <= 20;
            """)
    List<StockCyqPerfDay> getLatest20DaysData();

}
