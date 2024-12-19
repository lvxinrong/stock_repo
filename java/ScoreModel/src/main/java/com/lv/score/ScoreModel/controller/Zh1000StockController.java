package com.lv.score.ScoreModel.controller;

import com.lv.score.ScoreModel.calculate.entity.PageInfo;
import com.lv.score.ScoreModel.calculate.save.CalculateResultMonthEsEntity;
import com.lv.score.ScoreModel.calculate.save.SearchResultInEs;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.lv.score.ScoreModel.constant.EsIndexNameConstant.ZH1000_INDEX_NAME;
import static com.lv.score.ScoreModel.constant.EsIndexNameConstant.ZH800_INDEX_NAME;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lvxinrong
 * @since 2024-11-20
 */
@RestController
@RequestMapping("/zh1000Stock")
public class Zh1000StockController {

    @Autowired
    SearchResultInEs searchResultInEs;

    @Value("${stock.stock_month}")
    private String stockMonthDefault;

    @GetMapping("/score")
    public PageInfo<CalculateResultMonthEsEntity> getMonthScore(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                                @RequestParam(value = "stockMonth", required = false) String stockMonth) throws IOException {
        String indexName = ZH1000_INDEX_NAME + (StringUtils.isBlank(stockMonth) ? stockMonthDefault : stockMonth);
        return searchResultInEs.searchWithPagination(indexName, page, pageSize);
    }
}
