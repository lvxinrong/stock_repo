package com.lv.score.ScoreModel.cal_core.index;

import com.lv.score.ScoreModel.entity.IndexBasicDaily;

import java.util.List;

public interface IndexStockService {

    List<IndexBasicDaily> getIndexTradeDaily(String indexCode);
}
