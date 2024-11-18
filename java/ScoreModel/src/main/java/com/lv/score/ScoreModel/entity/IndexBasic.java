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
@TableName("index_basic")
public class IndexBasic implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tsCode;

    private String name;

    private String market;

    private String publisher;

    private String category;

    private String baseDate;

    private String basePoint;

    private String listDate;

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

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(String baseDate) {
        this.baseDate = baseDate;
    }

    public String getBasePoint() {
        return basePoint;
    }

    public void setBasePoint(String basePoint) {
        this.basePoint = basePoint;
    }

    public String getListDate() {
        return listDate;
    }

    public void setListDate(String listDate) {
        this.listDate = listDate;
    }

    @Override
    public String toString() {
        return "IndexBasic{" +
        "tsCode = " + tsCode +
        ", name = " + name +
        ", market = " + market +
        ", publisher = " + publisher +
        ", category = " + category +
        ", baseDate = " + baseDate +
        ", basePoint = " + basePoint +
        ", listDate = " + listDate +
        "}";
    }
}
