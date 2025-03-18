package com.lv.score.ScoreModel.service;

import com.lv.score.ScoreModel.entity.LimitDownThsDaily;
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
public interface ILimitDownThsDailyService extends IService<LimitDownThsDaily> {

    /**
     * 获取当日涨停板数据
     * @return
     */
    List<LimitDownThsDaily> getCurrentDate();

    String latestDay();

}
