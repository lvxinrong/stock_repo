package com.lv.score.ScoreModel.cal_core.save.impl;

import com.lv.score.ScoreModel.cal_core.save.SaveCalResult;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultDaily;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultMonth;
import com.lv.score.ScoreModel.calculate.save.CalculateResultDailyEsEntity;
import com.lv.score.ScoreModel.calculate.save.CalculateResultMonthEsEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class SaveCalResultImpl implements SaveCalResult {

    @Autowired
    SaveMontScoreToEs saveMontScoreToEs;

    @Autowired
    SaveDailyScoreToES saveDailyScoreToES;

    @Value("${stock.stock_month}")
    private String stock_month;

    @Override
    public void saveDailyScoreToES(Map<String, List<CalculateResultDaily>> calResultMap) {
        // es中 document按照tsCode存储
        List<CalculateResultDailyEsEntity> saveList = new ArrayList<>();
        calResultMap.values().forEach(e -> {
            saveList.addAll(e.stream().map(CalculateResultDailyEsEntity::new).toList());
        });
        try {
            saveDailyScoreToES.bulkSave(saveList);
        } catch (IOException e) {
            log.error("saveMonthScoreToES Exception;", e);
        }

    }

    @Override
    public void saveDailyScoreToES(Map<String, List<CalculateResultDaily>> calResultMap, String indexCode) {
        List<CalculateResultDailyEsEntity> saveList = new ArrayList<>();
        calResultMap.values().forEach(e -> {
            saveList.addAll(e.stream().map(CalculateResultDailyEsEntity::new).toList());
        });
        try {
            saveDailyScoreToES.bulkSave(saveList, indexCode);
        } catch (IOException e) {
            log.error("saveMonthScoreToES Exception;", e);
        }
    }

    @Override
    public void saveMonthScoreToES(List<CalculateResultMonth> montScoreList) {
        List<CalculateResultMonthEsEntity> saveList = montScoreList.stream().map(CalculateResultMonthEsEntity::new).toList();
        try {
            saveMontScoreToEs.bulkSave(saveList);
        } catch (IOException e) {
            log.error("saveMonthScoreToES Exception;", e);
        }
    }

    @Override
    public void saveMonthScoreToES(List<CalculateResultMonth> montScoreList, String indexCode) {
        List<CalculateResultMonthEsEntity> saveList = montScoreList.stream().map(CalculateResultMonthEsEntity::new).toList();
        try {
            saveMontScoreToEs.bulkSave(saveList, indexCode);
        } catch (IOException e) {
            log.error("saveMonthScoreToES Exception;", e);
        }
    }
}
