package com.lv.score.ScoreModel.controller;

import com.lv.score.ScoreModel.entity.LimitUpCountEntity;
import com.lv.score.ScoreModel.entity.LimitUpThsDaily;
import com.lv.score.ScoreModel.service.ILimitUpThsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-13
 */
@RestController
@RequestMapping("/limitUpThsDaily")
public class LimitUpThsDailyController {

    @Autowired
    ILimitUpThsDailyService iLimitUpThsDailyService;

    @GetMapping("/getCurrentDate")
    public List<LimitUpThsDaily> getCurrentDate() {
        return iLimitUpThsDailyService.getCurrentDate();
    }

    /**
     * 返回过去30天的涨停日期和涨停数量
     */
    @GetMapping("/getLast60UpDate")
    public Map<String, List<Object>> getLast60UpDate() {
        Map<String, List<Object>> ans = new HashMap<>();
        ans.put("dateList", new ArrayList<>());
        ans.put("countList", new ArrayList<>());
        List<LimitUpCountEntity> queryResult = iLimitUpThsDailyService.getLast60UpDate();
        for(LimitUpCountEntity entity : queryResult) {
            ans.get("dateList").add(entity.getTradeDate());
            ans.get("countList").add(entity.getCountSize());
        }
        return ans;
    }

}
