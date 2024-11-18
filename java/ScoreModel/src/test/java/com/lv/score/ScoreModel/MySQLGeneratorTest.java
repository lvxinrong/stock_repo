package com.lv.score.ScoreModel;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import org.junit.jupiter.api.Test;

public class MySQLGeneratorTest extends BaseGeneratorTest {

    /**
     * 数据源配置
     */
    private static final DataSourceConfig DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:mysql://127.0.0.1:3306/good_stock?useSSL=false&serverTimezone=Asia/Shanghai", "root", "123456")
            .schema("good_stock")
            .build();

    @Test
    public void testSimple() {
        AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
        generator.packageInfo(new PackageConfig.Builder().parent("com.lv.score.ScoreModel").build());
        generator.strategy(strategyConfig().build());
        generator.global(globalConfig().build());
        generator.execute();
    }
}
