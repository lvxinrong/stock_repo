package com.lv.score.ScoreModel.mapper;

import com.lv.score.ScoreModel.entity.LimitUpCountEntity;
import com.lv.score.ScoreModel.entity.LimitUpThsDaily;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-13
 */
public interface LimitUpThsDailyMapper extends BaseMapper<LimitUpThsDaily> {

    String getLatestDay();

    List<LimitUpCountEntity> getLast60UpDate();
}
