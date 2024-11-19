package com.lv.score.ScoreModel.calculate.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class PageInfo<T> {

    Integer page;
    Integer pageSize;
    List<T> items;
    Long total;
}
