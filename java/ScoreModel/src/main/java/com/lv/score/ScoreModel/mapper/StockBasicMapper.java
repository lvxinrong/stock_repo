package com.lv.score.ScoreModel.mapper;

import com.lv.score.ScoreModel.entity.StockBasic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2024-11-15
 */
public interface StockBasicMapper extends BaseMapper<StockBasic> {

    int countNum();

}
