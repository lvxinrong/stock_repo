package com.lv.score.ScoreModel;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lv.score.ScoreModel.mapper.DailyBasicInfoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DailyBasicInfoMapperTest {

    @Autowired
    DailyBasicInfoMapper dailyBasicInfoMapper;

    @Test
    public void testSelect() {
        System.out.println(dailyBasicInfoMapper.selectCount(new QueryWrapper<>()));
    }

}
