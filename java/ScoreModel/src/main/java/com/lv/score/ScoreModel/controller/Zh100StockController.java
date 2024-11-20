package com.lv.score.ScoreModel.controller;

import com.lv.score.ScoreModel.calculate.entity.PageInfo;
import com.lv.score.ScoreModel.calculate.save.CalculateResultMonthEsEntity;
import com.lv.score.ScoreModel.calculate.save.SearchResultInEs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.lv.score.ScoreModel.constant.EsIndexNameConstant.HS300_INDEX_NAME;
import static com.lv.score.ScoreModel.constant.EsIndexNameConstant.ZH100_INDEX_NAME;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lvxinrong
 * @since 2024-11-20
 */
@RestController
@RequestMapping("/zh100Stock")
public class Zh100StockController {

    @Autowired
    SearchResultInEs searchResultInEs;

    @GetMapping("/score")
    public PageInfo<CalculateResultMonthEsEntity> getMonthScore(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) throws IOException {
        return searchResultInEs.searchWithPagination(ZH100_INDEX_NAME, page, pageSize);
    }

}
