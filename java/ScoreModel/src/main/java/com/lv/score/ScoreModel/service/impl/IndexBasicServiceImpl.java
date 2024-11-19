package com.lv.score.ScoreModel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lv.score.ScoreModel.entity.IndexBasic;
import com.lv.score.ScoreModel.mapper.IndexBasicMapper;
import com.lv.score.ScoreModel.service.IIndexBasicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-11-15
 */
@Service
public class IndexBasicServiceImpl extends ServiceImpl<IndexBasicMapper, IndexBasic> implements IIndexBasicService {

    @Override
    public String selectIndexNameByCode(String indexCode) {
        QueryWrapper<IndexBasic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ts_code", indexCode);
        List<IndexBasic> queryList = list(queryWrapper);
        return queryList.size() > 0 ? queryList.get(0).getName() : "未知";
    }
}
