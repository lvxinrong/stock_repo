package com.lv.score.ScoreModel.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lv.score.ScoreModel.entity.StockStkFactorData;
import com.lv.score.ScoreModel.entity.page_plus.PageDTO;
import com.lv.score.ScoreModel.entity.query.StockSktFactorDataQuery;
import com.lv.score.ScoreModel.service.IStockStkFactorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 股票技术因子 前端控制器
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-18
 */
@RestController
@RequestMapping("/stockStkFactorData")
public class StockStkFactorDataController {

    @Autowired
    IStockStkFactorDataService iStockStkFactorDataService;

    @GetMapping("/getDataByPage")
    public IPage<StockStkFactorData> queryStockStkFactorData(StockSktFactorDataQuery stockSktFactorDataQuery) {
        return iStockStkFactorDataService.queryByPage(stockSktFactorDataQuery);
    }

}
