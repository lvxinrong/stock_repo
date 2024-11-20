package com.lv.score.ScoreModel.service;

import com.lv.score.ScoreModel.entity.Zh500Stock;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lvxinrong
 * @since 2024-11-20
 */
public interface IZh500StockService extends IService<Zh500Stock> {

    String getLastTradeDate();

    List<Zh500Stock> getTradeDateStockList(String tradeDate);

}
