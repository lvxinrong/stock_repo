package com.lv.score.ScoreModel.controller;

import com.lv.score.ScoreModel.entity.IndMoneyFlowDcDay;
import com.lv.score.ScoreModel.service.IIndMoneyFlowDcDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 板块资金流向（DC） 前端控制器
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-17
 */
@RestController
@RequestMapping("/indMoneyFlowDcDay")
public class IndMoneyFlowDcDayController {

    @Autowired
    IIndMoneyFlowDcDayService iIndMoneyFlowDcDayService;

    @GetMapping("/getListByTradeDate")
    public List<IndMoneyFlowDcDay> getListByTradeDate(@RequestParam(value = "tradeDate") String tradeDate) {
        return iIndMoneyFlowDcDayService.getListByTradeDate(tradeDate);
    }

}
