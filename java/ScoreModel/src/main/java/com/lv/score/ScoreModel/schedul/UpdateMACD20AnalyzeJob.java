package com.lv.score.ScoreModel.schedul;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.lv.score.ScoreModel.stock_strategy.macd.MACDStrategyExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class UpdateMACD20AnalyzeJob {

    @Autowired
    MACDStrategyExecutor macdStrategyExecutor;

    @Autowired
    ElasticsearchClient elasticsearchClient;

    @Scheduled(cron = "0 0 20 * * ?")
    public void executeGenerateMACD20() {
        log.info("========== 开始执行晚间定时任务 ==========");
        // 1. 判断当天交易日的数据是否已经同步，并且es中不存在已计算的数据
        if (!isExist()) {
            macdStrategyExecutor.generateMACD20DayReport2ES();
        } else {
            log.warn("已存在当天的策略记录，不再执行生成方法");
        }
        LocalDateTime now = LocalDateTime.now();
        log.info("当前时间：{}", now.format(DateTimeFormatter.ISO_LOCAL_TIME));
        log.info("===== 任务执行完毕 =====");
    }

    private boolean isExist() {
        String currDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        ExistsRequest index = new ExistsRequest.Builder()
                .index(MACDStrategyExecutor.ES_INDEX_NAME + "_" + currDate)
                .build();

        BooleanResponse response = null;
        try {
            response = elasticsearchClient.indices().exists(index);
            return response.value();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
