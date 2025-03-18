package com.lv.score.ScoreModel.controller;

import com.lv.score.ScoreModel.deepseek.LimitUpDeepSeekResult;
import com.lv.score.ScoreModel.entity.LimitUpDeepseekAnalyzeResult;
import com.lv.score.ScoreModel.service.ILimitUpDeepseekAnalyzeResultService;
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
 * @since 2025-03-14
 */
@RestController
@RequestMapping("/limitUpDeepseekAnalyzeResult")
public class LimitUpDeepseekAnalyzeResultController {

    @Autowired
    ILimitUpDeepseekAnalyzeResultService iLimitUpDeepseekAnalyzeResultService;

    @GetMapping("/getLatestDate")
    public List<LimitUpDeepseekAnalyzeResult> getLatestDate() {
        return iLimitUpDeepseekAnalyzeResultService.getLatestDate();
    }
}
