package com.lv.score.ScoreModel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lv.score.ScoreModel.entity.Zh100Stock;
import com.lv.score.ScoreModel.entity.Zh500Stock;
import com.lv.score.ScoreModel.entity.Zh800Stock;
import com.lv.score.ScoreModel.mapper.Zh800StockMapper;
import com.lv.score.ScoreModel.service.IZh800StockService;
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
public class Zh800StockServiceImpl extends ServiceImpl<Zh800StockMapper, Zh800Stock> implements IZh800StockService {

    @Override
    public String getLastTradeDate() {
        QueryWrapper<Zh800Stock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eqSql("trade_date", "select MAX(trade_date) from zh_800_stock");
        Zh800Stock zh800Stock = baseMapper.selectList(queryWrapper).get(0);
        return zh800Stock.getTradeDate();
    }

    @Override
    public List<Zh800Stock> getTradeDateStockList(String tradeDate) {
        // 如果是yyyymmdd 格式
        if (tradeDate.length() > 6) {
            QueryWrapper<Zh800Stock> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("trade_date", tradeDate + "%");
            return baseMapper.selectList(queryWrapper);
        } else {
            // yyyymm 格式，先查询该月最新的tradeDate,然后用该数据进行查询
            QueryWrapper<Zh800Stock> queryWrapper = new QueryWrapper<>();
            queryWrapper.eqSql("trade_date", "select MAX(trade_date) from zh_800_stock where trade_date like '" + tradeDate +"%'");
            List<Zh800Stock> stocks = baseMapper.selectList(queryWrapper);
            if (stocks.size() == 0) {
                log.warn("zh_100_stock表中 TradeDate: {}, 查无数据", tradeDate);
                return Collections.emptyList();
            }
            Zh800Stock zh800Stock = baseMapper.selectList(queryWrapper).get(0);
            String tradeDateLatest = zh800Stock.getTradeDate();
            QueryWrapper<Zh100Stock> wrapper = new QueryWrapper<>();
            wrapper.eq("trade_date", tradeDateLatest);
            return baseMapper.selectList(queryWrapper);
        }
    }
}
