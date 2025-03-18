package com.lv.score.ScoreModel.controller;

import com.lv.score.ScoreModel.entity.MktMoneyFlowDcDay;
import com.lv.score.ScoreModel.service.IMktMoneyFlowDcDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 大盘资金流向（DC） 前端控制器
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-17
 */
@RestController
@RequestMapping("/mktMoneyFlowDcDay")
public class MktMoneyFlowDcDayController {

    @Autowired
    IMktMoneyFlowDcDayService iMktMoneyFlowDcDayService;

    @GetMapping("/getLast90DayDate")
    public List<MktMoneyFlowDcDay> getLast90DayDate() {
        return iMktMoneyFlowDcDayService.getLast90DayDate();
    }
}
