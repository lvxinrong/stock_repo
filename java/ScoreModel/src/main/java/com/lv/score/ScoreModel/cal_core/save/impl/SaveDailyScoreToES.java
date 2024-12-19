package com.lv.score.ScoreModel.cal_core.save.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import com.lv.score.ScoreModel.calculate.save.CalculateResultDailyEsEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class SaveDailyScoreToES {

    public static final String PRODUCTS = "daily_score";

    @Value("${stock.stock_month}")
    private String stockMonth;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public void bulkSave(List<CalculateResultDailyEsEntity> products) throws IOException {
        BulkRequest.Builder br = new BulkRequest.Builder();
        products.forEach(product->br.operations(operation->
                operation.index(i->i
                        .index(PRODUCTS)
                        .id(product.getId())
                        .document(product))));
        elasticsearchClient.bulk(br.build());
    }

    public void bulkSave(List<CalculateResultDailyEsEntity> products, String indexCode) throws IOException {
        BulkRequest.Builder br = new BulkRequest.Builder();
        products.forEach(product->br.operations(operation->
                operation.index(i->i
                        .index(indexCode + "_" + PRODUCTS + "_" + stockMonth)
                        .id(product.getId())
                        .document(product))));
        BulkResponse response = elasticsearchClient.bulk(br.build());
        log.info("save response: {}", response);
    }

}
