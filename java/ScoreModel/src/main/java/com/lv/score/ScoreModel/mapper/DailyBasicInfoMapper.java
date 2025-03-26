package com.lv.score.ScoreModel.mapper;

import com.lv.score.ScoreModel.entity.DailyBasicInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 股票交易及财务数据 Mapper 接口
 * </p>
 *
 * @author lvxinrong
 * @since 2024-11-19
 */
@Mapper
public interface DailyBasicInfoMapper extends BaseMapper<DailyBasicInfo> {

    @Select("select MAX(trade_date) from daily_basic_info")
    String getLatestDate();

    @Select("select * from daily_basic_info where ts_code=#{tsCode} order by trade_date desc limit 1")
    DailyBasicInfo getLatestDataByTsCode(String tsCode);

}
