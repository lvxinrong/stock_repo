package com.lv.score.ScoreModel.entity;

import com.lv.score.ScoreModel.stock_strategy.stock_cyq.entity.StockCyqConcentratedEsDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class StockCyqConcentratedEsVO {

    private String tsCode;
    // 计算期间，最新的交易日期
    private String lastTradeDate;
    // 当前价格
    private BigDecimal currentPrice;
    // 指标计算
    private List<BigDecimal> convergeRatio;

    private BigDecimal lastConvergeRatio;
    // 收敛趋势验证
    private Boolean convergeValid;
    // 主力行为验证
    private Boolean controlValid;
    //4. 量价验证
    private Boolean priceValid;

    private Boolean isGood;

    private String stockName;

    public StockCyqConcentratedEsVO(StockCyqConcentratedEsDTO dto) {
        this.tsCode = dto.getTsCode();
        this.lastTradeDate = dto.getLastTradeDate();
        this.currentPrice = dto.getCurrentPrice();
        this.convergeRatio = new ArrayList<>(dto.getConvergeRatio());
        this.lastConvergeRatio = dto.getLastConvergeRatio();
        this.convergeValid = dto.getConvergeValid();
        this.controlValid = dto.getControlValid();
        this.priceValid = dto.getPriceValid();
        this.isGood = dto.getIsGood();
    }
}
