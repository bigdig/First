package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
// @MapperScan("com.ty.mapper")
@ComponentScan(basePackages = { "com" })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}