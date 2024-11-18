package com.lv.score.ScoreModel.calculate.save;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class SaveMontScoreToEs {

    public static final String PRODUCTS = "month_score";

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public String bulkSave(List<CalculateResultMonthEsEntity> products) throws IOException {
        BulkRequest.Builder br = new BulkRequest.Builder();
        products.stream().forEach(product->br.operations(operation->
                operation.index(i->i
                        .index(PRODUCTS)
                        .id(product.getTs_code())
                        .document(product))));

        BulkResponse response =elasticsearchClient.bulk(br.build());
        if(response.errors()){
            return new StringBuffer("Bulk has errors").toString();
        } else {
            return new StringBuffer("Bulk save success").toString();
        }
    }
}
