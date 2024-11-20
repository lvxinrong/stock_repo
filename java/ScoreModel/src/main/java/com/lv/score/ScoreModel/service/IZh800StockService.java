package com.lv.score.ScoreModel.service;

import com.lv.score.ScoreModel.entity.Zh500Stock;
import com.lv.score.ScoreModel.entity.Zh800Stock;
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
public interface IZh800StockService extends IService<Zh800Stock> {

    String getLastTradeDate();

    List<Zh800Stock> getTradeDateStockList(String tradeDate);

}
