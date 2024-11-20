package com.lv.score.ScoreModel.cal_core.save;

import com.lv.score.ScoreModel.calculate.entity.CalculateResultDaily;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultMonth;

import java.util.List;
import java.util.Map;

public interface SaveCalResult {

    void saveDailyScoreToES(Map<String, List<CalculateResultDaily>> calResultMap);

    void saveDailyScoreToES(Map<String, List<CalculateResultDaily>> calResultMap, String indexCode);

    void saveMonthScoreToES(List<CalculateResultMonth> montScoreList);

    void saveMonthScoreToES(List<CalculateResultMonth> montScoreList, String indexCode);
}
