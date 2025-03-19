package com.lv.score.ScoreModel.stock_strategy.macd;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import com.lv.score.ScoreModel.stock_strategy.macd.entity.MACD20DayAnalysisResult;
import com.lv.score.ScoreModel.stock_strategy.macd.entity.MACD20EsResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class MACDStrategyExecutor {

    @Autowired
    StockMACDCalculateInterface stockMACDCalculateInterface;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    private static final String ES_INDEX_NAME = "macd20";


    /**
     * 计算20日的macd结果
     * 根据macd结果生成报告和买入建议
     * 将结果写入到es中
     */
    public void generateMACD20DayReport2ES() {
        Map<String, MACD20DayAnalysisResult> calResult = stockMACDCalculateInterface.getLatest20DaysData();
        try {
            save2ES(calResult.values().stream().map(MACD20EsResult::new).toList());
        }catch (Exception e) {
            log.error("insert into es error", e);
        }
    }

    private void save2ES(List<MACD20EsResult> products) throws IOException {
        String calDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        BulkRequest.Builder br = new BulkRequest.Builder();
        products.forEach(product->br.operations(operation->
                operation.index(i->i
                        .index( ES_INDEX_NAME + "_" + product.getLastTradeDate())
                        .id(product.getId())
                        .document(product))));
        BulkResponse response = elasticsearchClient.bulk(br.build());
        log.info("save response: {}", response);
    }
}
