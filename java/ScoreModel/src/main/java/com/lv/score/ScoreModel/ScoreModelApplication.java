package com.lv.score.ScoreModel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lv.score.ScoreModel.mapper")
public class ScoreModelApplication {


	/**
	 * 关关难过关关过，步步难行步步行。
	 * 夜夜难熬夜夜熬，事事难成事事成。
	 * 前路漫漫亦灿灿，往事堪堪亦澜澜！
	 */
	public static void main(String[] args) {
		SpringApplication.run(ScoreModelApplication.class, args);
	}

}
