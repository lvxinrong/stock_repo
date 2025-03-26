package com.lv.score.ScoreModel.stock_strategy.macd.selector.enitiy;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
public class MACD_KDJ_SelectorResult {

    private String tsCode;
    private boolean isMACD_KDJ_GoldenCross;  // 双金叉状态
    private String macdTrendStatus;         // MACD趋势强度（如："STRONG_BULL"）
    private BigDecimal latestKDJ_J;         // 最新J值
    private boolean hasBullDivergence;      // 是否出现底背离
    private Map<String, Object> indicators; // 详细指标快照（示例数据见下文）
}
