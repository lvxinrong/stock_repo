package com.lv.score.ScoreModel.controller;

import com.lv.score.ScoreModel.entity.LimitDownThsDaily;
import com.lv.score.ScoreModel.entity.LimitUpThsDaily;
import com.lv.score.ScoreModel.service.ILimitDownThsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-13
 */
@RestController
@RequestMapping("/limitDownThsDaily")
public class LimitDownThsDailyController {

    @Autowired
    ILimitDownThsDailyService iLimitDownThsDailyService;

    @GetMapping("/getCurrentDate")
    public List<LimitDownThsDaily> getCurrentDate() {
        return iLimitDownThsDailyService.getCurrentDate();
    }

}
