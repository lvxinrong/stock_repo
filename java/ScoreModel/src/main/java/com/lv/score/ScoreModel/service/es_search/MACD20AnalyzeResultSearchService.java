package com.lv.score.ScoreModel.service.es_search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.lv.score.ScoreModel.calculate.entity.PageInfo;
import com.lv.score.ScoreModel.entity.DailyBasicInfo;
import com.lv.score.ScoreModel.entity.MACD20EsResultVO;
import com.lv.score.ScoreModel.service.IDailyBasicInfoService;
import com.lv.score.ScoreModel.service.IStockBasicService;
import com.lv.score.ScoreModel.stock_strategy.macd.entity.MACD20EsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
public class MACD20AnalyzeResultSearchService {

    @Autowired
    ElasticsearchClient elasticsearchClient;

    @Autowired
    IStockBasicService iStockBasicService;

    @Autowired
    IDailyBasicInfoService iDailyBasicInfoService;

    // 查询参数常量
    private static final String TS_CODE = "tsCode";
    private static final String TREND_DIRECTION = "trendDirection";
    private static final String TREND_STRENGTH_MIN = "trendStrengthMin";
    private static final String TREND_STRENGTH_MAX = "trendStrengthMax";
    private static final String MA_ALIGNMENT = "maAlignment";
    private static final String MOMENTUM_VALID = "momentumValid";
    private static final String VOLUME_BREAK = "volumeBreak";
    private static final String MULTI_FILTER_PASSED = "multiFilterPassed";
    private static final String BUY_SIGNAL = "buySignal";

    private final String indexName = "macd20_";

    private final static MathContext mc = new MathContext(8, RoundingMode.HALF_UP);

    public PageInfo<MACD20EsResultVO> search(Map<String, Object> conditions, int page, int size) throws IOException {
        // 构建Bool查询
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        // 处理精确匹配查询
        handleTermQuery(boolQueryBuilder, conditions, TS_CODE, TS_CODE + ".keyword");
        handleTermQuery(boolQueryBuilder, conditions, TREND_DIRECTION, TREND_DIRECTION + ".keyword");
        handleTermQuery(boolQueryBuilder, conditions, MA_ALIGNMENT, MA_ALIGNMENT + ".keyword");

        // 处理范围查询
        handleRangeQuery(boolQueryBuilder, conditions);

        // 处理布尔值查询
        handleBoolTermQuery(boolQueryBuilder, conditions, MOMENTUM_VALID);
        handleBoolTermQuery(boolQueryBuilder, conditions, VOLUME_BREAK);
        handleBoolTermQuery(boolQueryBuilder, conditions, MULTI_FILTER_PASSED);
        handleBoolTermQuery(boolQueryBuilder, conditions, BUY_SIGNAL);

        // 构建分页参数
        int from = page * size;

        // 构建SearchRequest
        SearchRequest searchRequest = SearchRequest.of(builder -> builder
                .index(indexName + conditions.get("tradeDate"))
                .from(from)
                .size(size)
                .query(Query.of(q -> q.bool(boolQueryBuilder.build())))
                .sort(s -> s.field(f -> f.field("trendStrength").order(SortOrder.Desc)))
        );

        SearchResponse<MACD20EsResult> response = elasticsearchClient.search(searchRequest, MACD20EsResult.class);

        List<MACD20EsResult> result = response.hits().hits().stream()
                .map(Hit::source)
                .toList();
        List<MACD20EsResultVO> voList = result.stream().map(e -> {
            MACD20EsResultVO vo = new MACD20EsResultVO(e);
            vo.setStockName(iStockBasicService.queryByTsCode(vo.getTsCode()).getName());
            vo.setCumulativeIncrease(BigDecimal.valueOf(getCumulativeIncrease(e.getTsCode(), e.getLastTradeDate())));
            vo.setTotalVm(getTotalVm(e.getTsCode()));
            return vo;
        }).toList();
        // 构建响应
        PageInfo<MACD20EsResultVO> pageResult = new PageInfo<>();
        pageResult.setPage(page);
        pageResult.setPageSize(size);
        pageResult.setItems(voList);
        pageResult.setTotal(response.hits().total().value());
        return pageResult;
    }

    private void handleTermQuery(BoolQuery.Builder boolBuilder,
                                 Map<String, Object> conditions,
                                 String conditionKey,
                                 String fieldName) {
        if (conditions.containsKey(conditionKey)) {
            Object value = conditions.get(conditionKey);
            if (value != null) {
                boolBuilder.must(Query.of(q -> q
                        .term(TermQuery.of(t -> t
                                .field(fieldName)
                                .value(value.toString()))
                        )));
            }
        }
    }

    private void handleBoolTermQuery(BoolQuery.Builder boolBuilder,
                                     Map<String, Object> conditions,
                                     String conditionKey) {
        if (conditions.containsKey(conditionKey)) {
            Object value = conditions.get(conditionKey);
            if (value != null) {
                boolBuilder.must(Query.of(q -> q
                        .term(TermQuery.of(t -> t
                                .field(conditionKey)
                                .value(Boolean.parseBoolean(value.toString()))
                        ))));
            }
        }
    }

    private void handleRangeQuery(BoolQuery.Builder boolBuilder,
                                  Map<String, Object> conditions) {
        if (conditions.containsKey(TREND_STRENGTH_MIN) || conditions.containsKey(TREND_STRENGTH_MAX)) {
            // 修改点1：使用JsonData包装数值
            JsonData min = convertToJsonData(conditions.get(TREND_STRENGTH_MIN));
            JsonData max = convertToJsonData(conditions.get(TREND_STRENGTH_MAX));

            RangeQuery.Builder rangeBuilder = new RangeQuery.Builder()
                    .field("trendStrength");

            // 修改点2：使用JsonData参数
            if (min != null) rangeBuilder.gte(min);
            if (max != null) rangeBuilder.lte(max);

            boolBuilder.must(Query.of(q -> q.range(rangeBuilder.build())));
        }
    }

    // 新增转换方法
    private JsonData convertToJsonData(Object value) {
        if (value == null) return null;

        if (value instanceof BigDecimal) {
            return JsonData.of(((BigDecimal) value).doubleValue());
        }
        if (value instanceof Number) {
            return JsonData.of(((Number) value).doubleValue());
        }
        if (value instanceof String) {
            try {
                return JsonData.of(Double.parseDouble((String) value));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private Double parseDouble(Object value) {
        if (value == null) return null;
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).doubleValue();
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 计算格式：
     * macd计算日期 close  -> 最新的交易日close
     * 计算涨跌幅
     */
    private double getCumulativeIncrease(String tsCode, String tradeDate) {
        DailyBasicInfo start = iDailyBasicInfoService.getBasicInfoByTsCodeAndTradeDate(tsCode, tradeDate);
        DailyBasicInfo end = iDailyBasicInfoService.getLatestData(tsCode);
        if (start == null || end == null) {
            return 0.0;
        }

        double startClose = start.getClose();
        double endClose = end.getClose();

        return (endClose - startClose) / startClose * 100;
    }

    /**
     * 查询股票的当日市值
     */
    public double getTotalVm(String tsCode) {
        DailyBasicInfo dailyBasicInfo = iDailyBasicInfoService.getLatestData(tsCode);
        // 单位万元
        double totalVm = dailyBasicInfo.getTotalMv();
        // 转换为亿元
        return totalVm / 10000;
    }

}
