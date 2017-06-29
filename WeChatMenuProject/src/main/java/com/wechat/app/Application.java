package com.wechat.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
//@PropertySources({ @PropertySource("classpath:application.properties")/*,@PropertySource("classpath:spring-mvc.xml")*/ })

public class Application {
/**
 * 启动器
 * 
 * @param args
 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

