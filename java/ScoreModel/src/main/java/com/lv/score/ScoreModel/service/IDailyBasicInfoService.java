package com.lv.score.ScoreModel.service;

import com.lv.score.ScoreModel.entity.DailyBasicInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 股票交易及财务数据 服务类
 * </p>
 *
 * @author lvxinrong
 * @since 2024-11-19
 */
public interface IDailyBasicInfoService extends IService<DailyBasicInfo> {

    DailyBasicInfo getLatestData(String tsCode);

}
