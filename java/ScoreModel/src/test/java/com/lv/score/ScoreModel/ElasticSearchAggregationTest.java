package com.lv.score.ScoreModel;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import com.lv.score.ScoreModel.stock_strategy.macd.entity.MACD20DayAnalysisResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
public class ElasticSearchAggregationTest {

    @Autowired
    ElasticsearchClient elasticsearchClient;

    @Test
    public void testCount() throws IOException {
        List<String> indexNameList = getLatest20MacdIndices();
        List<String> tsCodeList = Arrays.asList("301348.SZ", "688212.SH");
        // 1. 构建查询条件
        BoolQuery boolQuery = BoolQuery.of(b -> b
                .must(Query.of(q -> q
                        .terms(TermsQuery.of(t -> t
                                .field("tsCode.keyword")
                                .terms(t2 -> t2.value(tsCodeList.stream()
                                        .map(FieldValue::of)
                                        .collect(Collectors.toList())))
                        ))
                ))
                .must(Query.of(q -> q
                        .term(TermQuery.of(t -> t
                                .field("buySignal")
                                .value(true)
                        ))
                ))
        );

        // 2. 构建聚合
        Aggregation aggregation = Aggregation.of(a -> a
                .terms(t -> t
                        .field("tsCode.keyword")
                        .size(tsCodeList.size())
                )
        );

        // 3. 构建搜索请求
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(indexNameList)
                .query(Query.of(q -> q.bool(boolQuery)))
                .aggregations("stock_code_count", aggregation)
                .size(0) // 不需要返回文档，只需要聚合结果
        );

        // 4. 执行查询
        SearchResponse<Void> response = elasticsearchClient.search(searchRequest, Void.class);

        // 解析聚合结果
        Map<String, Long> result = new HashMap<>();
        // 5. 处理聚合结果
        StringTermsAggregate terms = response.aggregations()
                .get("stock_code_count")
                .sterms();
        for (StringTermsBucket bucket : terms.buckets().array()) {
            result.put(bucket.key().stringValue(), bucket.docCount());
        }

        // 确保所有查询的股票代码都在结果中（出现次数为0的也要包含）
        for (String code : tsCodeList) {
            result.putIfAbsent(code, 0L);
        }

    }

    /**
     * 获取最新的20个macd20_*索引（按创建时间倒序）
     */
    private List<String> getLatest20MacdIndices() throws IOException {
        // 获取所有macd20_开头的索引
        GetIndexResponse indices = elasticsearchClient.indices().get(b -> b.index("macd20_*"));

        // 按创建时间排序并取前20个
        return indices.result().keySet().stream()
                .sorted(Comparator.reverseOrder())
                .limit(20)
                .collect(Collectors.toList());
    }
}
