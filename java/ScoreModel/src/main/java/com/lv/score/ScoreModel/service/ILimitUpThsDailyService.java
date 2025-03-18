package com.lv.score.ScoreModel.service;

import com.lv.score.ScoreModel.entity.LimitUpCountEntity;
import com.lv.score.ScoreModel.entity.LimitUpThsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-13
 */
public interface ILimitUpThsDailyService extends IService<LimitUpThsDaily> {

    /**
     * 获取当日涨停板数据
     * @return
     */
    List<LimitUpThsDaily> getCurrentDate();

    String latestDay();

    List<LimitUpCountEntity> getLast60UpDate();

}
