package com.lv.score.ScoreModel.service.impl;

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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lv.score.ScoreModel.calculate.entity.PageInfo;
import com.lv.score.ScoreModel.entity.MACD20EsResultVO;
import com.lv.score.ScoreModel.entity.StockCyqConcentratedEsVO;
import com.lv.score.ScoreModel.entity.StockCyqPerfDay;
import com.lv.score.ScoreModel.entity.StockStkFactorData;
import com.lv.score.ScoreModel.entity.query.StockCyqPerfDayQuery;
import com.lv.score.ScoreModel.mapper.StockCyqPerfDayMapper;
import com.lv.score.ScoreModel.service.IStockBasicService;
import com.lv.score.ScoreModel.service.IStockCyqPerfDayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lv.score.ScoreModel.stock_strategy.macd.entity.MACD20EsResult;
import com.lv.score.ScoreModel.stock_strategy.stock_cyq.StockCyqPerfExecutor;
import com.lv.score.ScoreModel.stock_strategy.stock_cyq.entity.StockCyqConcentratedEsDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 每日筹码及胜率 服务实现类
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-18
 */
@Service
public class StockCyqPerfDayServiceImpl extends ServiceImpl<StockCyqPerfDayMapper, StockCyqPerfDay> implements IStockCyqPerfDayService {

    @Autowired
    StockCyqPerfDayMapper stockCyqPerfDayMapper;

    @Autowired
    ElasticsearchClient elasticsearchClient;

    @Autowired
    IStockBasicService iStockBasicService;

    @Autowired


    @Override
    public List<StockCyqPerfDay> getLatestDateList() {
        QueryWrapper<StockCyqPerfDay> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("trade_date", latestDay());
        return list(queryWrapper);
    }

    @Override
    public String latestDay() {
        return this.baseMapper.getLatestDay();
    }

    @Override
    public List<StockCyqPerfDay> getTradeDateList(String tradeDate) {
        QueryWrapper<StockCyqPerfDay> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("trade_date", tradeDate);
        return list(queryWrapper);
    }

    @Override
    public IPage<StockCyqPerfDay> getDataByPage(StockCyqPerfDayQuery stockCyqPerfDayQuery) {
        IPage<StockCyqPerfDay> page = new Page<>(stockCyqPerfDayQuery.getPage(), stockCyqPerfDayQuery.getSize());
        QueryWrapper<StockCyqPerfDay> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(stockCyqPerfDayQuery.getTradeDate())) {
            queryWrapper.eq("trade_date", stockCyqPerfDayQuery.getTradeDate());
        }
        if(StringUtils.isNotEmpty(stockCyqPerfDayQuery.getTsCode())) {
            queryWrapper.eq("ts_code", stockCyqPerfDayQuery.getTsCode());
        }
        stockCyqPerfDayMapper.selectPage(page, queryWrapper);
        return page;
    }

    @Override
    public List<StockCyqPerfDay> getLatest5DaysData() {
        return stockCyqPerfDayMapper.getLatest5DaysData();
    }

    @Override
    public List<StockCyqPerfDay> getLatest20DaysData() {
        return stockCyqPerfDayMapper.getLatest20DaysData();
    }

    @Override
    public PageInfo<StockCyqConcentratedEsVO> search(Map<String, Object> conditions, int page, int size) throws IOException {
        // 构建Bool查询
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        // 处理精确匹配查询
        handleTermQuery(boolQueryBuilder, conditions, "tsCode", "tsCode" + ".keyword");

        // 处理布尔值查询
        handleBoolTermQuery(boolQueryBuilder, conditions, "isGood");

        // 构建分页参数
        int from = page * size;

        // 构建SearchRequest
        SearchRequest searchRequest = SearchRequest.of(builder -> builder
                .index(StockCyqPerfExecutor.INDEX_NAME + "_" + conditions.get("tradeDate"))
                .from(from)
                .size(size)
                .query(Query.of(q -> q.bool(boolQueryBuilder.build())))
        );

        SearchResponse<StockCyqConcentratedEsDTO> response = elasticsearchClient.search(searchRequest, StockCyqConcentratedEsDTO.class);

        List<StockCyqConcentratedEsDTO> result = response.hits().hits().stream()
                .map(Hit::source)
                .toList();

        List<StockCyqConcentratedEsVO> voList = result.stream().map(e -> {
            StockCyqConcentratedEsVO vo = new StockCyqConcentratedEsVO(e);
            vo.setStockName(iStockBasicService.getStockNameByTsCode(e.getTsCode()));
            return vo;
        }).toList();
        // 构建响应
        PageInfo<StockCyqConcentratedEsVO> pageResult = new PageInfo<>();
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






}
