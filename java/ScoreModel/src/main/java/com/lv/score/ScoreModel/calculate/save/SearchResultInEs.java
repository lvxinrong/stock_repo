package com.lv.score.ScoreModel.calculate.save;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SearchResultInEs {

    public static final String PRODUCTS = "month_score";

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public List<CalculateResultMonthEsEntity> findAll() throws IOException {
        SearchRequest request = SearchRequest.of(s->s.index(PRODUCTS));
        SearchResponse<CalculateResultMonthEsEntity> response = elasticsearchClient.search(request,CalculateResultMonthEsEntity.class);

        List<CalculateResultMonthEsEntity> products = new ArrayList<>();
        response.hits().hits().forEach(object->{
            products.add(object.source());
        });
        return products;

    }

    public List<CalculateResultMonthEsEntity> top20() throws IOException {
        SearchRequest request = SearchRequest.of(s->s.index(PRODUCTS).size(20).sort(sort -> sort.field(f-> f.field("score").order(SortOrder.Desc))));
        SearchResponse<CalculateResultMonthEsEntity> response = elasticsearchClient.search(request,CalculateResultMonthEsEntity.class);

        List<CalculateResultMonthEsEntity> products = new ArrayList<>();
        response.hits().hits().forEach(object->{
            products.add(object.source());
        });
        return products;
    }
}
