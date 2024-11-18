package com.lv.score.ScoreModel;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
public class ElasticSearchTest {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Test
    public void create_index() throws IOException {
        CreateIndexRequest request = CreateIndexRequest.of(builder -> builder.index("user"));
        CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(request);
        log.info("acknowledged: {}", createIndexResponse.acknowledged());
        log.info("index: {}", createIndexResponse.index());
        log.info("shardsAcknowledged: {}", createIndexResponse.shardsAcknowledged());
    }

    @Test
    void get_index_test() throws IOException {
        List<String> indexName = new ArrayList<>();
        indexName.add("user");
        GetIndexRequest request = GetIndexRequest.of(builder -> builder.index(indexName));
        GetIndexResponse getIndexResponse = elasticsearchClient.indices().get(request);
        Map<String, IndexState> result =
                getIndexResponse.result();
        result.forEach((key, value) -> log.info("key: {}, value: {}", key, value));
    }
}
