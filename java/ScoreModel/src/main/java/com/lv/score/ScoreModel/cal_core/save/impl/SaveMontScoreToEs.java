package com.lv.score.ScoreModel.cal_core.save.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import com.lv.score.ScoreModel.calculate.save.CalculateResultMonthEsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class SaveMontScoreToEs {

    public static final String PRODUCTS = "month_score";

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public void bulkSave(List<CalculateResultMonthEsEntity> products) throws IOException {
        BulkRequest.Builder br = new BulkRequest.Builder();
        products.forEach(product->br.operations(operation->
                operation.index(i->i
                        .index(PRODUCTS)
                        .id(product.getTs_code())
                        .document(product))));
        elasticsearchClient.bulk(br.build());
    }

    public void bulkSave(List<CalculateResultMonthEsEntity> products, String indexCode) throws IOException {
        BulkRequest.Builder br = new BulkRequest.Builder();
        products.forEach(product->br.operations(operation->
                operation.index(i->i
                        .index(indexCode + "_" + PRODUCTS)
                        .id(product.getTs_code())
                        .document(product))));
        elasticsearchClient.bulk(br.build());
    }
}
