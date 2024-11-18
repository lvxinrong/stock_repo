package com.lv.score.ScoreModel.controller;

import com.lv.score.ScoreModel.calculate.Calculate;
import com.lv.score.ScoreModel.calculate.entity.CalculateResultMonth;
import com.lv.score.ScoreModel.calculate.save.CalculateResultMonthEsEntity;
import com.lv.score.ScoreModel.calculate.save.SearchResultInEs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/score")
public class ScoreController {

    @Autowired
    Calculate calculate;

    @Autowired
    SearchResultInEs searchResultInEs;

    @GetMapping("/getScore")
    public List<CalculateResultMonthEsEntity> getScore() throws IOException {
        return searchResultInEs.findAll();
    }

    @GetMapping("/top20")
    public List<CalculateResultMonthEsEntity> getTop20() throws IOException {
        return searchResultInEs.top20();
    }
}
