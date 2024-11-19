package com.lv.score.ScoreModel.cal_core.index;

import com.lv.score.ScoreModel.cal_core.entity.IndexStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class QueryIndexStockFactoryService {

    @Autowired
    private Map<String, QueryIndexStockService> serverMap;

    public List<IndexStock> getIndexStock(String indexCode) {
        QueryIndexStockService queryIndexStockService = serverMap.get(indexCode);
        if (queryIndexStockService == null) {
            return new ArrayList<>();
        }
        return queryIndexStockService.getIndexStockList();
    }

}
