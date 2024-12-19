package com.lv.score.ScoreModel.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EsIndexNameConstant {

    //沪深300 月分数
    public static final String HS300_INDEX_NAME = "399300_month_score" + "_" ;
    //沪深300 交易日分数
    public static final String HS300_INDEX_NAME_DAILY = "399300_daily_score" + "_";

    //中证100 月分数
    public static final String ZH100_INDEX_NAME = "000903_month_score" + "_" ;

    //中证500 月分数
    public static final String ZH500_INDEX_NAME = "000905_month_score" + "_";

    //中证800 月分数
    public static final String ZH800_INDEX_NAME = "000906_month_score" + "_";

    //中证1000 月分数
    public static final String ZH1000_INDEX_NAME = "000852_month_score" + "_";

}
