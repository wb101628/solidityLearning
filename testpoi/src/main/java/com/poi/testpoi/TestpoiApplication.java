package com.poi.testpoi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = {"com.poi.testpoi.mapper"})
public class TestpoiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestpoiApplication.class, args);
	}
}
