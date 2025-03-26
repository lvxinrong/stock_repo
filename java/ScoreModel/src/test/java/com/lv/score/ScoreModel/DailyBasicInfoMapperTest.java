package com.lv.score.ScoreModel;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lv.score.ScoreModel.mapper.DailyBasicInfoMapper;
import com.lv.score.ScoreModel.mapper.LimitUpThsDailyMapper;
import com.lv.score.ScoreModel.service.ILimitUpThsDailyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DailyBasicInfoMapperTest {

    @Autowired
    DailyBasicInfoMapper dailyBasicInfoMapper;

    @Autowired
    ILimitUpThsDailyService iLimitUpThsDailyService;

    @Autowired
    LimitUpThsDailyMapper limitUpThsDailyMapper;

    @Test
    public void testSelect() {
        System.out.println(dailyBasicInfoMapper.selectCount(new QueryWrapper<>()));
    }

    @Test
    public void testSelectLimit() {
        System.out.println(iLimitUpThsDailyService.getCurrentDate());
    }

    @Test
    public void testS() {
        System.out.println(iLimitUpThsDailyService.latestDay());
    }

    @Test
    public void test1() {
        System.out.println(limitUpThsDailyMapper.getLast60UpDate());
    }

    @Test
    public void test111() {
        System.out.println(dailyBasicInfoMapper.getLatestDataByTsCode("000001.SZ").getTradeDate());
    }

}
