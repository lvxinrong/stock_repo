package com.lv.score.ScoreModel.service;

import com.lv.score.ScoreModel.entity.Hs300Stock;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lvxinrong
 * @since 2024-11-19
 */
public interface IHs300StockService extends IService<Hs300Stock> {

    String getLastTradeDate();

    List<Hs300Stock> getTradeDateStockList(String tradeDate);

}
