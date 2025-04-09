package com.lv.score.ScoreModel.stock_strategy.macd.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.MultiBucketBase;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import com.lv.score.ScoreModel.entity.StockStkFactorData;
import com.lv.score.ScoreModel.service.IStockStkFactorDataService;
import com.lv.score.ScoreModel.stock_strategy.macd.analyzers.MACD20DayAnalyzer;
import com.lv.score.ScoreModel.stock_strategy.macd.analyzers.MACD5DayAnalyzer;
import com.lv.score.ScoreModel.stock_strategy.macd.IStockMACDCalculateInterface;
import com.lv.score.ScoreModel.stock_strategy.macd.entity.MACD20DayAnalysisResult;
import com.lv.score.ScoreModel.stock_strategy.macd.entity.MACD5DayAnalysisResult;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IStockMACDCalculateInterfaceImpl implements IStockMACDCalculateInterface {

    @Autowired
    IStockStkFactorDataService iStockStkFactorDataService;

    @Autowired
    ElasticsearchClient elasticsearchClient;

    @Override
    public Map<String, MACD5DayAnalysisResult> getLatest5DaysData() {
        List<StockStkFactorData> data = iStockStkFactorDataService.getLast5DaysData();
        Map<String, List<StockStkFactorData>> map = data.stream().collect(Collectors.groupingBy(StockStkFactorData::getTsCode));
        Map<String, MACD5DayAnalysisResult> result = new HashMap<>();
        String lastDataMysql = this.iStockStkFactorDataService.getLatestDate();
        for (Map.Entry<String, List<StockStkFactorData>> entry : map.entrySet()) {
            try {
                if (entry.getValue().get(0).getTradeDate().equals(lastDataMysql)) {
                    String tsCode = entry.getKey();
                    MACD5DayAnalysisResult macd5DayAnalysisResult = MACD5DayAnalyzer.analyzeLast5Days(entry.getValue());
                    result.put(tsCode, macd5DayAnalysisResult);
                } else {
                    log.warn("股票: {}, 技术因子数据缺失，不是最新的数据。", entry.getKey());
                }
            } catch (Exception e) {
                log.error("calculate MACD error. tsCode:{}", entry.getKey(), e);
            }
        }
        return result;
    }

    @Override
    public Map<String, MACD20DayAnalysisResult> getLatest20DaysData() {
        List<StockStkFactorData> data = iStockStkFactorDataService.getLast20DaysData();
        return getCalData(data, false);
    }

    @Override
    public Map<String, MACD20DayAnalysisResult> get20DaysData(String calData) {
        List<StockStkFactorData> data = iStockStkFactorDataService.get20DaysData(calData);
        return getCalData(data, true);
    }

    private Map<String, MACD20DayAnalysisResult> getCalData(List<StockStkFactorData> data, boolean isValidDate) {
        Map<String, List<StockStkFactorData>> map = data.stream().collect(Collectors.groupingBy(StockStkFactorData::getTsCode));
        Map<String, MACD20DayAnalysisResult> result = new HashMap<>();
        String lastDataMysql = this.iStockStkFactorDataService.getLatestDate();
        for (Map.Entry<String, List<StockStkFactorData>> entry : map.entrySet()) {
            try {
                if (isValidDate || lastDataMysql.equals(entry.getValue().get(0).getTradeDate())) {
                    String tsCode = entry.getKey();
                    MACD20DayAnalysisResult macd20DayAnalysisResult = MACD20DayAnalyzer.analyze20Day(entry.getValue());
                    cal20DayResult(macd20DayAnalysisResult, entry.getValue());
                    result.put(tsCode, macd20DayAnalysisResult);
                } else {
                    log.warn("股票: {}, 技术因子数据缺失，不是最新的数据。", entry.getKey());
                }
            } catch (Exception e) {
                log.error("calculate MACD error. tsCode:{}", entry.getKey(), e);
            }
        }
        try {
            calTsCodeBuySignalCount(result);
        } catch (IOException e) {
            log.error("calTsCodeBuySignalCount IOException", e);
        }
        return result;
    }

    private void cal20DayResult(MACD20DayAnalysisResult result, List<StockStkFactorData> stockStkFactorDataList) {
        boolean buySignal = result.getTrendDirection().equals("UP")
                && result.getMaAlignment().equals("BULLISH")
                && result.getMomentumValid()
                && result.getVolumeBreak()
                && result.getTrendStrength().compareTo(new BigDecimal(5)) > 0
                && result.getMultiFilterPassed();
        result.setBuySignal(buySignal);
        StockStkFactorData last = stockStkFactorDataList.get(stockStkFactorDataList.size() - 1);
        if (buySignal) {
            String buyStringFormat = """
                    [交易信号] 符合买入条件：
                    标的: %s
                    价格: %s
                    建议仓位: %s
                    """;
            result.setBuyFromatString(String.format(buyStringFormat, last.getTsCode(), last.getClose(), calculatePositionSize(result)));
        } else {
            result.setBuyFromatString("[决策建议] 当前不符合买入条件");
        }
    }

    // 动态仓位计算（参考网页1的仓位管理）
    private static BigDecimal calculatePositionSize(MACD20DayAnalysisResult result) {
        BigDecimal base = new BigDecimal("100"); // 基础单位
        // 每5%趋势强度增加10%仓位（上限200%）
        BigDecimal ratio = result.getTrendStrength()
                .divide(new BigDecimal(5), 2, BigDecimal.ROUND_HALF_UP)
                .min(new BigDecimal(2));
        return base.multiply(BigDecimal.ONE.add(ratio));
    }

    private void calTsCodeBuySignalCount(Map<String, MACD20DayAnalysisResult> data) throws IOException {
        List<String> indexNameList = getLatest20MacdIndices();
        List<String> tsCodeList = data.keySet().stream().toList();
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

        for(Map.Entry<String, MACD20DayAnalysisResult> entry : data.entrySet()) {
            Long count = result.get(entry.getKey());
            entry.getValue().setBuySignalTrueCount(count.intValue());
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
