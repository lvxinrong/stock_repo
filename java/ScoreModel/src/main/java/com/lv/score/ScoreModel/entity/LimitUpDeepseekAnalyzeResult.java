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
 * @since 2025-03-14
 */
@Getter
@Setter
@TableName("limit_up_deepseek_analyze_result")
public class LimitUpDeepseekAnalyzeResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 股票代码
     */
    private String tsCode;

    /**
     * 交易日期
     */
    private String tradeDate;

    /**
     * 分析结果
     */
    private String analyzeResult;

    /**
     * deepseek打分
     */
    private String score;
}
