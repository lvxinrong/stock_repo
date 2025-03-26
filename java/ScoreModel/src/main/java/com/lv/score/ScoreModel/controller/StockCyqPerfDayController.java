package com.lv.score.ScoreModel.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lv.score.ScoreModel.calculate.entity.PageInfo;
import com.lv.score.ScoreModel.entity.StockCyqConcentratedEsVO;
import com.lv.score.ScoreModel.entity.StockCyqPerfDay;
import com.lv.score.ScoreModel.entity.query.StockCyqPerfDayQuery;
import com.lv.score.ScoreModel.entity.query.StockSktFactorDataQuery;
import com.lv.score.ScoreModel.service.IStockCyqPerfDayService;
import com.lv.score.ScoreModel.stock_strategy.stock_cyq.entity.StockCyqConcentratedEsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 每日筹码及胜率 前端控制器
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-18
 */
@RestController
@RequestMapping("/stockCyqPerfDay")
@Slf4j
public class StockCyqPerfDayController {

    @Autowired
    IStockCyqPerfDayService iStockCyqPerfDayService;


    @GetMapping("/getLatestDateList")
    public List<StockCyqPerfDay> getLatestDateList() {
        return iStockCyqPerfDayService.getLatestDateList();
    }

    @GetMapping("/getTradeDateList")
    public List<StockCyqPerfDay> getTradeDateList(@RequestParam(value = "tradeDate")String tradeDate) {
        return  iStockCyqPerfDayService.getTradeDateList(tradeDate);
    }

    @GetMapping("/getDataByPage")
    public IPage<StockCyqPerfDay> getDataByPage(StockCyqPerfDayQuery stockCyqPerfDayQuery) {
        return iStockCyqPerfDayService.getDataByPage(stockCyqPerfDayQuery);
    }

    @GetMapping("/getAnalyzeDataByPage")
    public PageInfo<StockCyqConcentratedEsVO> getAnalyzeDataByPage(@RequestParam Map<String, Object> searchConditions,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        try {
            return iStockCyqPerfDayService.search(searchConditions, page, size);
        } catch (IOException e) {
            log.error("getAnalyzeDataByPage exception. ", e);
        }
        return null;
    }

}
