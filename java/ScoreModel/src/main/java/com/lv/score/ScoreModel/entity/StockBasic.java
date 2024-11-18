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
@TableName("stock_basic")
public class StockBasic implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tsCode;

    private String symbol;

    private String name;

    private String area;

    private String industry;

    private String fullname;

    private String enname;

    private String cnspell;

    private String market;

    private String exchange;

    private String currType;

    private String listStatus;

    private String listDate;

    private String delistDate;

    private String isHs;

    public String getTsCode() {
        return tsCode;
    }

    public void setTsCode(String tsCode) {
        this.tsCode = tsCode;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEnname() {
        return enname;
    }

    public void setEnname(String enname) {
        this.enname = enname;
    }

    public String getCnspell() {
        return cnspell;
    }

    public void setCnspell(String cnspell) {
        this.cnspell = cnspell;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getCurrType() {
        return currType;
    }

    public void setCurrType(String currType) {
        this.currType = currType;
    }

    public String getListStatus() {
        return listStatus;
    }

    public void setListStatus(String listStatus) {
        this.listStatus = listStatus;
    }

    public String getListDate() {
        return listDate;
    }

    public void setListDate(String listDate) {
        this.listDate = listDate;
    }

    public String getDelistDate() {
        return delistDate;
    }

    public void setDelistDate(String delistDate) {
        this.delistDate = delistDate;
    }

    public String getIsHs() {
        return isHs;
    }

    public void setIsHs(String isHs) {
        this.isHs = isHs;
    }

    @Override
    public String toString() {
        return "StockBasic{" +
        "tsCode = " + tsCode +
        ", symbol = " + symbol +
        ", name = " + name +
        ", area = " + area +
        ", industry = " + industry +
        ", fullname = " + fullname +
        ", enname = " + enname +
        ", cnspell = " + cnspell +
        ", market = " + market +
        ", exchange = " + exchange +
        ", currType = " + currType +
        ", listStatus = " + listStatus +
        ", listDate = " + listDate +
        ", delistDate = " + delistDate +
        ", isHs = " + isHs +
        "}";
    }
}
