package com.lv.score.ScoreModel.service;

import com.lv.score.ScoreModel.entity.Zh1000Stock;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lv.score.ScoreModel.entity.Zh800Stock;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lvxinrong
 * @since 2024-11-20
 */
public interface IZh1000StockService extends IService<Zh1000Stock> {

    String getLastTradeDate();

    List<Zh1000Stock> getTradeDateStockList(String tradeDate);

}
