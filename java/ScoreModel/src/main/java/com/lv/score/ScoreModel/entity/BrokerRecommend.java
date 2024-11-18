package com.lv.score.ScoreModel.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2024-11-15
 */
@TableName("broker_recommend")
public class BrokerRecommend implements Serializable {

    private static final long serialVersionUID = 1L;

    private String month;

    private String broker;

    private String tsCode;

    private String name;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getTsCode() {
        return tsCode;
    }

    public void setTsCode(String tsCode) {
        this.tsCode = tsCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BrokerRecommend{" +
        "month = " + month +
        ", broker = " + broker +
        ", tsCode = " + tsCode +
        ", name = " + name +
        "}";
    }
}
