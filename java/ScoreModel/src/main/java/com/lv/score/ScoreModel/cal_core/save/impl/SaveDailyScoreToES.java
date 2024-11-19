package com.lv.score.ScoreModel.cal_core.save.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import com.lv.score.ScoreModel.calculate.save.CalculateResultDailyEsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class SaveDailyScoreToES {

    public static final String PRODUCTS = "daily_score";

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

}
