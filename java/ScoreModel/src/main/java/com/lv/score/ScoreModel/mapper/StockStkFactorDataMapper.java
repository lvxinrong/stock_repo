package com.lv.score.ScoreModel.mapper;

import com.lv.score.ScoreModel.entity.StockStkFactorData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 股票技术因子 Mapper 接口
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-18
 */
@Mapper
public interface StockStkFactorDataMapper extends BaseMapper<StockStkFactorData> {

    @Select("""
            SELECT *
            FROM (
                SELECT
                    *,
                    ROW_NUMBER() OVER (PARTITION BY ts_code ORDER BY trade_date ASC) AS rn
                FROM stock_stk_factor_data
            ) sub
            WHERE rn <= 5;
            """)
    List<StockStkFactorData> getLast5DaysData();


    @Select("""
            SELECT *
            FROM (
                SELECT
                    *,
                    ROW_NUMBER() OVER (PARTITION BY ts_code ORDER BY trade_date DESC) AS rn
                FROM stock_stk_factor_data
            ) sub
            WHERE rn <= 20;
            """)
    List<StockStkFactorData> getLast20DaysData();

    @Select("SELECT MAX(trade_date) from stock_stk_factor_data")
    String getLastDate();

}
