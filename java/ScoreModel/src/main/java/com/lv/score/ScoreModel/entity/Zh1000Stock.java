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
 * @since 2024-11-20
 */
@Getter
@Setter
@TableName("zh_1000_stock")
public class Zh1000Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 指数代码
     */
    private String indexCode;

    /**
     * 成分代码
     */
    private String conCode;

    /**
     * 交易日期
     */
    private String tradeDate;

    /**
     * 权重
     */
    private Double weight;
}
