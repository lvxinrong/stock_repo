package com.lv.score.ScoreModel.stock_strategy.kdj;

import com.lv.score.ScoreModel.entity.StockStkFactorData;
import com.lv.score.ScoreModel.stock_strategy.kdj.entity.KDJDayAnalysisResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KDJCalculator {

    /**
     * period: 周期，一般是9，14,20
     */
    public static List<KDJDayAnalysisResult> calculateEnhancedKDJ(List<StockStkFactorData> data, int period) {
        Collections.reverse(data);
        List<KDJDayAnalysisResult> results = new ArrayList<>();
        double k = 50.0, d = 50.0;
        KDJDayAnalysisResult value = null;

        for (int i = period - 1; i < data.size(); i++) {
            double rsv = calculateRSV(data, i, period);

            // 更新K、D值
            k = (2.0/3)*k + (1.0/3)*rsv;
            d = (2.0/3)*d + (1.0/3)*k;
            double j = 3*k - 2*d;

            // 判断信号
            boolean overbought = j > 80;
            boolean oversold = j < 20;

            // 金叉死叉判断
            boolean goldenCross = false;
            boolean deadCross = false;
            if (value != null) {
                goldenCross = (k > d) && (value.getK() <= value.getD());
                deadCross = (k < d) && (value.getK() >= value.getD());
            }

            // 创建结果对象
            KDJDayAnalysisResult current = new KDJDayAnalysisResult(
                    data.get(i).getTsCode(),
                    data.get(i).getTradeDate(),
                    rsv,
                    k,
                    d,
                    j,
                    overbought,
                    oversold,
                    goldenCross,
                    deadCross
            );
            results.add(current);
            value = current;
        }
        return results;
    }

    private static double calculateRSV(List<StockStkFactorData> data, int endIndex, int period) {
        double min = Double.MIN_VALUE;
        double max = Double.MAX_VALUE;

        // 查找周期内极值
        for (int i = endIndex-period+1; i <= endIndex; i++) {
            min = Math.min(min, data.get(i).getLow().doubleValue());
            max = Math.max(max, data.get(i).getHigh().doubleValue());
        }

        double close = data.get(endIndex).getClose().doubleValue();
        return (max == min) ? 50 : ((close - min) / (max - min)) * 100;
    }
}
