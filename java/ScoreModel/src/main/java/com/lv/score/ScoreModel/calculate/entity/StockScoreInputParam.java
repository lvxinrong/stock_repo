package com.lv.score.ScoreModel.calculate.entity;

import com.lv.score.ScoreModel.entity.IndexBasicDaily;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class StockScoreInputParam {

    private String ts_code;
    //yyyyMM
    private String date;

    private String index_code;
    // 缓存使用
    List<IndexBasicDaily> indexBasicDailies;
}
