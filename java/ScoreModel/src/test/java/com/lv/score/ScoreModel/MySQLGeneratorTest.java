package com.lv.score.ScoreModel;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

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
        // 自定义路径
        Map<OutputFile, String> customPathInfo = new EnumMap<>(OutputFile.class);
        // 项目路径
        String projectPath = System.getProperty("user.dir");
        // src/main/java
        String srcMain = String.join(File.separator, "src", "main", "java");
        // 包名
        String packageName = "com.lv.score.ScoreModel".replace('.', File.separatorChar);
        // 自定义控制器路径
        customPathInfo.put(OutputFile.controller, String.join(File.separator, projectPath, srcMain, packageName));
        System.out.println(customPathInfo);
        generator.packageInfo(new PackageConfig.Builder().parent("com.lv.score.ScoreModel").pathInfo(customPathInfo).build());
        generator.strategy(strategyConfig().addInclude("cyz_100_stock","daily_basic_info", "hs_300_stock").build());
        generator.global(globalConfig().build());
        generator.execute();
    }

    @Test
    public void fastAutoGenerator() {
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/good_stock?useSSL=false&serverTimezone=Asia/Shanghai", "root", "123456")
                .globalConfig(builder -> builder
                        .author("lvxinrong")
                        .outputDir(Paths.get(System.getProperty("user.dir")) + "/src/main/java")
                        .commentDate("yyyy-MM-dd")
                )
                .packageConfig(builder -> builder
                        .parent("com.lv.score.ScoreModel")
                        .entity("entity")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .xml("mapper.xml")
                        .pathInfo(Collections.singletonMap(OutputFile.xml, "H:\\stock\\stock_repo\\java\\ScoreModel\\src\\main\\resources\\xml"))
                )
                .strategyConfig(builder -> builder.addInclude("cyz_100_stock","daily_basic_info", "hs_300_stock")
                        .entityBuilder()
                        .enableLombok()
                )
//                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
