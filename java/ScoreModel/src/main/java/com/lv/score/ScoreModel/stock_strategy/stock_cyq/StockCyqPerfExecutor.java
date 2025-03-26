package com.lv.score.ScoreModel.stock_strategy.stock_cyq;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import com.lv.score.ScoreModel.stock_strategy.stock_cyq.entity.StockCyqConcentratedEsDTO;
import com.lv.score.ScoreModel.stock_strategy.stock_cyq.entity.StockCyqConcentratedResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class StockCyqPerfExecutor {

    @Autowired
    IStockCyqPerfStrategyInterface iStockCyqPerfStrategyInterface;

    @Autowired
    ElasticsearchClient elasticsearchClient;

    public final static String INDEX_NAME = "stock_cyq_20";

    public void saveLast20DataToEs() {
        List<StockCyqConcentratedResult> analyzeResult = iStockCyqPerfStrategyInterface.getLast20DaysStock();
        List<StockCyqConcentratedEsDTO> saveResult = analyzeResult.stream().map(StockCyqConcentratedEsDTO::new).toList();
        try {
            saveEs(saveResult);
        } catch (IOException e) {
            log.error("save stock cyq 20 analyze data exception.", e);
        }
    }

    private void saveEs(List<StockCyqConcentratedEsDTO> products) throws IOException {
        BulkRequest.Builder br = new BulkRequest.Builder();
        products.forEach(product->br.operations(operation->
                operation.index(i->i
                        .index( INDEX_NAME + "_" + product.getLastTradeDate())
                        .id(product.getId())
                        .document(product))));
        BulkResponse response = elasticsearchClient.bulk(br.build());
        log.info("save response: {}", response);
    }
}
