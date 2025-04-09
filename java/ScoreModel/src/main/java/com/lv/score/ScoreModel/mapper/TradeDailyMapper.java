package com.lv.score.ScoreModel.mapper;

import com.lv.score.ScoreModel.entity.TradeDaily;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2024-11-15
 */
@Mapper
public interface TradeDailyMapper extends BaseMapper<TradeDaily> {

    @Select("""
            WITH ranked_data AS (
                SELECT
                    *,
                    ROW_NUMBER() OVER (
                        PARTITION BY ts_code
                        ORDER BY trade_date DESC
                    ) AS rn
                FROM trade_daily
            )
            SELECT *
            FROM ranked_data
            WHERE rn <= #{period}
            ORDER BY ts_code, trade_date DESC;
            """)
    List<TradeDaily> getLastData(Integer period);

    @Select("""
            SELECT *
            FROM trade_daily
            WHERE ts_code = #{tsCode}
              AND trade_date > #{tradeDate}
            ORDER BY trade_date ASC
            LIMIT 1;
            """)
    TradeDaily getNextData(String tsCode, String tradeDate);

    @Select("""
            SELECT *
            FROM trade_daily
            WHERE ts_code = #{tsCode}
            ORDER BY trade_date DESC
            LIMIT 1;
            """)
    TradeDaily getLastDateByTsCode(String tsCode);

    @Select("""
            SELECT MAX(trade_date)
            from trade_daily
            """)
    String getLatestDate();

}
