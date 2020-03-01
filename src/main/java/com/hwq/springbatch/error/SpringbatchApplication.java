package com.hwq.springbatch.error;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //表示是一个springBoot的启动类
@EnableBatchProcessing
public class SpringbatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbatchApplication.class, args);
	}

}
