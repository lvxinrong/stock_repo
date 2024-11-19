package com.lv.score.ScoreModel.calCore.save;

import com.lv.score.ScoreModel.calculate.entity.CalculateResultDaily;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultMonth;

import java.util.List;
import java.util.Map;

public interface SaveCalResult {

    void saveDailyScoreToES(Map<String, List<CalculateResultDaily>> calResultMap);

    void saveMonthScoreToES(List<CalculateResultMonth> montScoreList);
}
