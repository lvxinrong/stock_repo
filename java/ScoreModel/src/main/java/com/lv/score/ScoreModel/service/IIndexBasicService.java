package com.lv.score.ScoreModel.service;

import com.lv.score.ScoreModel.entity.IndexBasic;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-11-15
 */
public interface IIndexBasicService extends IService<IndexBasic> {
    String selectIndexNameByCode(String indexCode);
}
