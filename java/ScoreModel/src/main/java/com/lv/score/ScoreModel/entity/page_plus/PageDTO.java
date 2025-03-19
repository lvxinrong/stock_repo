package com.lv.score.ScoreModel.entity.page_plus;

import lombok.Data;

import java.util.List;

@Data
public class PageDTO<T> {

    private Long total;

    private Long pages;

    private List<T> lists;
}
