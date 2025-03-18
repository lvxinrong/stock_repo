package com.lv.score.ScoreModel.controller;

import com.lv.score.ScoreModel.entity.IndMoneyFlowThsDay;
import com.lv.score.ScoreModel.service.IIndMoneyFlowThsDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 板块资金流向(同花顺) 前端控制器
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-17
 */
@RestController
@RequestMapping("/indMoneyFlowThsDay")
public class IndMoneyFlowThsDayController {

    @Autowired
    IIndMoneyFlowThsDayService iIndMoneyFlowThsDayService;

    @GetMapping("/getListByTradeDate")
    public List<IndMoneyFlowThsDay> getListByTradeDate(@RequestParam(value = "tradeDate") String tradeDate) {
        return iIndMoneyFlowThsDayService.getListByTradeDate(tradeDate);
    }

}
