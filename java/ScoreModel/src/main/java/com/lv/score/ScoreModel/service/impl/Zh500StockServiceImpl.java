package com.lv.score.ScoreModel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lv.score.ScoreModel.entity.Zh100Stock;
import com.lv.score.ScoreModel.entity.Zh500Stock;
import com.lv.score.ScoreModel.mapper.Zh500StockMapper;
import com.lv.score.ScoreModel.service.IZh500StockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lvxinrong
 * @since 2024-11-20
 */
@Service
@Slf4j
public class Zh500StockServiceImpl extends ServiceImpl<Zh500StockMapper, Zh500Stock> implements IZh500StockService {

    @Override
    public String getLastTradeDate() {
        QueryWrapper<Zh500Stock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eqSql("trade_date", "select MAX(trade_date) from zh_500_stock");
        Zh500Stock zh500Stock = baseMapper.selectList(queryWrapper).get(0);
        return zh500Stock.getTradeDate();
    }

    @Override
    public List<Zh500Stock> getTradeDateStockList(String tradeDate) {
        // 如果是yyyymmdd 格式
        if (tradeDate.length() > 6) {
            QueryWrapper<Zh500Stock> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("trade_date", tradeDate + "%");
            return baseMapper.selectList(queryWrapper);
        } else {
            // yyyymm 格式，先查询该月最新的tradeDate,然后用该数据进行查询
            QueryWrapper<Zh500Stock> queryWrapper = new QueryWrapper<>();
            queryWrapper.eqSql("trade_date", "select MAX(trade_date) from zh_500_stock where trade_date like '" + tradeDate +"%'");
            List<Zh500Stock> stocks = baseMapper.selectList(queryWrapper);
            if (stocks.size() == 0) {
                log.warn("zh_100_stock表中 TradeDate: {}, 查无数据", tradeDate);
                return Collections.emptyList();
            }
            Zh500Stock zh500Stock = baseMapper.selectList(queryWrapper).get(0);
            String tradeDateLatest = zh500Stock.getTradeDate();
            QueryWrapper<Zh100Stock> wrapper = new QueryWrapper<>();
            wrapper.eq("trade_date", tradeDateLatest);
            return baseMapper.selectList(queryWrapper);
        }
    }
}
