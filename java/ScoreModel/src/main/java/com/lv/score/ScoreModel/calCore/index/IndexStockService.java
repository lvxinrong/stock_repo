package com.lv.score.ScoreModel.calCore.index;

import com.lv.score.ScoreModel.calCore.entity.IndexStock;
import com.lv.score.ScoreModel.entity.IndexBasicDaily;
import com.lv.score.ScoreModel.entity.TradeDaily;

import java.util.List;

public interface IndexStockService {

    List<IndexBasicDaily> getIndexTradeDaily(String indexCode);
}
