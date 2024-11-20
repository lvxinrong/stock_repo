package com.lv.score.ScoreModel.service;

import com.lv.score.ScoreModel.entity.Zh100Stock;
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
public interface IZh100StockService extends IService<Zh100Stock> {

    String getLastTradeDate();

    List<Zh100Stock> getTradeDateStockList(String tradeDate);

}
