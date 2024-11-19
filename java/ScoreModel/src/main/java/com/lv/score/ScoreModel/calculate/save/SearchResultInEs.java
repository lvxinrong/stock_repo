package com.lv.score.ScoreModel.calculate.save;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.lv.score.ScoreModel.calculate.entity.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SearchResultInEs {

    public static final String INDEX_NAME = "month_score";

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public List<CalculateResultMonthEsEntity> findAll() throws IOException {
        SearchRequest request = SearchRequest.of(s -> s.index(INDEX_NAME));
        SearchResponse<CalculateResultMonthEsEntity> response = elasticsearchClient.search(request, CalculateResultMonthEsEntity.class);

        List<CalculateResultMonthEsEntity> products = new ArrayList<>();
        response.hits().hits().forEach(object -> {
            products.add(object.source());
        });
        return products;

    }

    public List<CalculateResultMonthEsEntity> top20() throws IOException {
        SearchRequest request = SearchRequest.of(s -> s.index(INDEX_NAME).size(20).sort(sort -> sort.field(f -> f.field("score").order(SortOrder.Desc))));
        SearchResponse<CalculateResultMonthEsEntity> response = elasticsearchClient.search(request, CalculateResultMonthEsEntity.class);

        List<CalculateResultMonthEsEntity> products = new ArrayList<>();
        response.hits().hits().forEach(object -> {
            products.add(object.source());
        });
        return products;
    }

    public PageInfo<CalculateResultMonthEsEntity> searchWithPagination(int page, int pageSize) throws IOException {
        // 计算 from 参数
        int from = (page - 1) * pageSize;

        // 构建搜索请求
        SearchRequest searchRequest = SearchRequest.of(builder -> builder
                .index(INDEX_NAME)
                .from(from)
                .size(pageSize)
                .query(query -> query
                        .matchAll(matchAllQuery -> matchAllQuery // 示例：Match All 查询
                        )
                )
                .sort(sort -> sort.field(field -> field.field("score").order(SortOrder.Desc)))
        );

        // 执行查询
        SearchResponse<CalculateResultMonthEsEntity> response = elasticsearchClient.search(searchRequest, CalculateResultMonthEsEntity.class);
        // 解析结果
        List<CalculateResultMonthEsEntity> queryValueList = new ArrayList<>();
        response.hits().hits().forEach(object -> {
            queryValueList.add(object.source());
        });
        queryValueList.forEach(e -> {
            BigDecimal bd = BigDecimal.valueOf(e.getScore());
            bd = bd.setScale(2, RoundingMode.HALF_UP); // 四舍五入
            e.setScore(bd.doubleValue());
        });

        // 构建响应
        PageInfo<CalculateResultMonthEsEntity> result = new PageInfo<>();
        result.setPage(page);
        result.setPageSize(pageSize);
        result.setItems(queryValueList);
        result.setTotal(response.hits().total().value());
        return result;
    }
}
