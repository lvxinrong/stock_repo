package com.lv.score.ScoreModel.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author lvxinrong
 * @since 2025-03-13
 */
@Getter
@Setter
@TableName("limit_down_ths_daily")
public class LimitDownThsDaily implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tradeDate;

    private String tsCode;

    private String name;

    private String price;

    private String pctChg;

    private String openNum;

    private String luDesc;

    private String limitType;

    private String tag;

    private String status;

    private String limitOrder;

    private String limitAmount;

    private String turnoverRate;

    private String freeFloat;

    private String luLimitOrder;

    private String limitUpSucRate;

    private String turnover;

    private String marketType;
}
