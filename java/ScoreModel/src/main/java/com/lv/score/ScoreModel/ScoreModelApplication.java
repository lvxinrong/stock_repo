package com.lv.score.ScoreModel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lv.score.ScoreModel.mapper")
public class ScoreModelApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScoreModelApplication.class, args);
	}

}
