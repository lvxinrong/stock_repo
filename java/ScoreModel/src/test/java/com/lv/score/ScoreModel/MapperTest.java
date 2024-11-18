package com.lv.score.ScoreModel;

import com.lv.score.ScoreModel.mapper.StockBasicMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MapperTest {

    @Autowired
    StockBasicMapper stockBasicMapper;

    @Test
    public void test() {
        System.out.println(stockBasicMapper.countNum());
    }


}
