package com.lv.score.ScoreModel;

public class testA {

    public static void main(String[] args) {
        String buyStringFormat = """
                    [交易信号] 符合买入条件：
                    标的: %s
                    价格: %s
                    建议仓位: %s
                    """;
        System.out.println(String.format(buyStringFormat, "a", "b", "c"));
    }
}
