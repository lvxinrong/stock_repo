package com.lv.score.ScoreModel.service;

import com.lv.score.ScoreModel.entity.StockBasic;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-11-15
 */
public interface IStockBasicService extends IService<StockBasic> {

    StockBasic queryByTsCode(String tsCode);

    List<StockBasic> getStockBasicByMarket(String market);

    List<StockBasic> getAllData();

    String getStockNameByTsCode(String tsCode);

}
