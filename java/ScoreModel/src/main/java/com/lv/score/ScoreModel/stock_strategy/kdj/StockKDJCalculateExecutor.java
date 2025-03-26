package com.lv.score.ScoreModel.stock_strategy.kdj;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import com.lv.score.ScoreModel.entity.StockStkFactorData;
import com.lv.score.ScoreModel.service.IStockStkFactorDataService;
import com.lv.score.ScoreModel.stock_strategy.kdj.entity.KDJDayAnalysisEsResultEntity;
import com.lv.score.ScoreModel.stock_strategy.kdj.entity.KDJDayAnalysisResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class StockKDJCalculateExecutor {

    @Autowired
    IStockStkFactorDataService iStockStkFactorDataService;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    private static final int mid = 14;

    private static final int low = 9;

    private static final int high = 21;

    private static final String ES_INDEX_NAME = "kdj14";


    /**
     * period:
     * 短线交易：5-9日
     * 中线交易：10-14日
     * 长线交易：15-21日
     */
    public void generate14PeriodKDJResult() {
        List<StockStkFactorData> calData = iStockStkFactorDataService.getLast20DaysData();
        Map<String, List<KDJDayAnalysisResult>> calResult = new HashMap<>();
        Map<String, List<StockStkFactorData>> mapValue = calData.stream().collect(Collectors.groupingBy(StockStkFactorData::getTsCode));
        String lastDataMysqlDate = this.iStockStkFactorDataService.getLatestDate();
        for (Map.Entry<String, List<StockStkFactorData>> entry : mapValue.entrySet()) {
            try {
                if (lastDataMysqlDate.equals(entry.getValue().get(0).getTradeDate())) {
                    String tsCode = entry.getKey();
                    List<StockStkFactorData> stockLast20DayData = entry.getValue();
                    List<KDJDayAnalysisResult> kdjDayAnalysisResults = KDJCalculator.calculateEnhancedKDJ(stockLast20DayData, mid);
                    calResult.put(tsCode, kdjDayAnalysisResults);
                } else {
                    log.warn("股票: {}, 技术因子数据缺失，不是最新的数据。", entry.getKey());
                }
            } catch (Exception e) {
                log.error("StockKDJCalculateInterface generate14PeriodKDJResult Exception. tsCode:{}", entry.getKey(), e);
            }
        }
        saveResult2Es(calResult, lastDataMysqlDate);
    }

    private void saveResult2Es(Map<String, List<KDJDayAnalysisResult>> saveData, String lastDataMysqlDate) {
        List<KDJDayAnalysisEsResultEntity> saveValue = new ArrayList<>();
        for(List<KDJDayAnalysisResult> list : saveData.values()) {
            saveValue.addAll(list.stream().map(KDJDayAnalysisEsResultEntity::new).toList());
        }
        try {
            saveResultList2Es(saveValue, lastDataMysqlDate);
        } catch (IOException e) {
            log.error("saveResult2Es() Exception", e);
        }
    }

    private void saveResultList2Es(List<KDJDayAnalysisEsResultEntity> saveDataList, String calDate) throws IOException {
        BulkRequest.Builder br = new BulkRequest.Builder();
        saveDataList.forEach(product->br.operations(operation->
                operation.index(i->i
                        .index( ES_INDEX_NAME + "_" + calDate)
                        .id(product.getId())
                        .document(product))));
        BulkResponse response = elasticsearchClient.bulk(br.build());
        log.info("save response: {}", response);
    }

}
