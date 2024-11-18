package com.lv.score.ScoreModel.calculate.util;

import java.util.HashMap;
import java.util.Map;

public class TsCodeIndexConstant {

    public static final Map<String, String> tsCodeMap = Map.of("创业板", "399006.SZ",
            "上证指数", "000001.SH",
            "深证指数", "399001.SZ");

    /**
     * 通过股票tsCode 返回所属板块
     * @param tsCode
     * @return
     */
    public static String getIndexTsCode(String tsCode) {
        if (tsCode.startsWith("60")) {
            return "000001.SH";
        }

        if (tsCode.startsWith("00")) {
            return "399001.SZ";
        }

        if (tsCode.startsWith("301") || tsCode.startsWith("300")) {
            return "399006.SZ";
        }

        return "";
    }
}
