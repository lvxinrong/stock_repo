package com.lv.score.ScoreModel.controller;

import com.lv.score.ScoreModel.entity.TradeDaily;
import com.lv.score.ScoreModel.service.ITradeDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2024-11-15
 */
@RestController
@RequestMapping("/tradeDaily")
public class TradeDailyController {

    @Autowired
    ITradeDailyService iTradeDailyService;

    @Value("${stock.stock_month}")
    private String tradeDate;

    @GetMapping("/getStockTradeList")
    public List<TradeDaily> getTradeDaily(@RequestParam(value = "ts_code")String ts_code) {
        return iTradeDailyService.getTradeDailyDataByTsCodeAndTradeDate(ts_code, tradeDate);
    }

}
