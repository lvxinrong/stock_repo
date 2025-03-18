package com.lv.score.ScoreModel.service;

import com.lv.score.ScoreModel.entity.LimitUpDeepseekAnalyzeResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-14
 */
public interface ILimitUpDeepseekAnalyzeResultService extends IService<LimitUpDeepseekAnalyzeResult> {

    List<LimitUpDeepseekAnalyzeResult> getLatestDate();
}
