package com.lv.score.ScoreModel.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lv.score.ScoreModel.calculate.entity.PageInfo;
import com.lv.score.ScoreModel.entity.MACD20EsResultVO;
import com.lv.score.ScoreModel.service.es_search.MACD20AnalyzeResultSearchService;
import com.lv.score.ScoreModel.stock_strategy.macd.entity.MACD20EsResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/macd20CalResultSearch")
@Slf4j
public class MACD20CalResultSearchController {


    @Autowired
    MACD20AnalyzeResultSearchService macd20AnalyzeResultSearchService;


    @GetMapping("/search")
    public PageInfo<MACD20EsResultVO> search(
            @RequestParam Map<String, Object> searchConditions,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageInfo<MACD20EsResultVO> result = null;
        try {
            result = macd20AnalyzeResultSearchService.search(searchConditions, page, size);
        } catch (IOException e) {
            log.error("MACD20CalResultSearchController search Exception", e);
        }
        return result;
    }
}
