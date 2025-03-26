package com.lv.score.ScoreModel.config;

import com.lv.score.ScoreModel.entity.StockBasic;
import com.lv.score.ScoreModel.service.IStockBasicService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RedisInitializer {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    IStockBasicService iStockBasicService;

    public static final String TSCODE_NAME_REDIS_KEY = "stock_code_map";

    @PostConstruct
    public void initData() {
        if (!isTestStart()) {
            List<StockBasic> allData = iStockBasicService.getAllData();
            // 检查 Key 是否存在
            if(Boolean.TRUE.equals(redisTemplate.hasKey(TSCODE_NAME_REDIS_KEY))) {
                // 删除旧 Key
                redisTemplate.delete(TSCODE_NAME_REDIS_KEY);
                log.info("已删除旧 Key: {}", TSCODE_NAME_REDIS_KEY);
            }
            Map<String, String> valueMap = allData.stream().collect(Collectors.toMap(StockBasic::getTsCode, StockBasic::getName));
            redisTemplate.opsForHash().putAll(TSCODE_NAME_REDIS_KEY, valueMap);
            log.info("成功初始化 {} 条股票数据", valueMap.size());
        } else {
            log.warn("单元测试启动，不再重新初始化");
        }
    }

    private boolean isTestStart() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return Arrays.stream(stackTrace)
                .anyMatch(element -> element.getClassName().contains("Test"));
    }
}
