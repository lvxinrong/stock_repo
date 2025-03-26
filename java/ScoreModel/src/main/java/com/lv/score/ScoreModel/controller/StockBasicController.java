package com.lv.score.ScoreModel.controller;

import com.lv.score.ScoreModel.entity.DailyBasicInfo;
import com.lv.score.ScoreModel.entity.StockBasic;
import com.lv.score.ScoreModel.entity.StockDailyBasicInfo;
import com.lv.score.ScoreModel.service.IDailyBasicInfoService;
import com.lv.score.ScoreModel.service.IStockBasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2024-11-15
 */
@RestController
@RequestMapping("/stockBasic")
public class StockBasicController {

    @Autowired
    IStockBasicService iStockBasicService;

    @Autowired
    IDailyBasicInfoService iDailyBasicInfoService;

    @GetMapping("/getStockBasicByTsCode")
    public StockDailyBasicInfo getStockBasicByTsCode(@RequestParam("tsCode")String tsCode) {
        return getResult(iStockBasicService.queryByTsCode(tsCode), iDailyBasicInfoService.getLatestData(tsCode));
    }

    private StockDailyBasicInfo getResult(StockBasic stockBasic, DailyBasicInfo dailyBasicInfo) {
        StockDailyBasicInfo stockDailyBasicInfo = new StockDailyBasicInfo();
        stockDailyBasicInfo.setName(stockBasic.getName());
        stockDailyBasicInfo.setListDate(stockBasic.getListDate());
        stockDailyBasicInfo.setPe(dailyBasicInfo.getPe());
        stockDailyBasicInfo.setClose(dailyBasicInfo.getClose());
        stockDailyBasicInfo.setTsCode(dailyBasicInfo.getTsCode());
        stockDailyBasicInfo.setCircMv(dailyBasicInfo.getCircMv());
        stockDailyBasicInfo.setTotalMv(dailyBasicInfo.getTotalMv());
        return stockDailyBasicInfo;
    }

}
