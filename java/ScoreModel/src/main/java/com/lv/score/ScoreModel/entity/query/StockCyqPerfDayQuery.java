package com.lv.score.ScoreModel.entity.query;

import com.lv.score.ScoreModel.entity.page_plus.PageQuery;
import lombok.Data;

@Data
public class StockCyqPerfDayQuery extends PageQuery {

    private String tradeDate;

    private String tsCode;
}
