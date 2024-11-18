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
@TableName("ths_index")
public class ThsIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tsCode;

    private String name;

    private String count;

    private String exchange;

    private String listDate;

    private String type;

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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getListDate() {
        return listDate;
    }

    public void setListDate(String listDate) {
        this.listDate = listDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ThsIndex{" +
        "tsCode = " + tsCode +
        ", name = " + name +
        ", count = " + count +
        ", exchange = " + exchange +
        ", listDate = " + listDate +
        ", type = " + type +
        "}";
    }
}
