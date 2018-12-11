package com.joo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Application {

	static final String PROPERTIES = "spring.config.location="
		+ "classpath:/thirdparty.request.properties"
		+ "classpath:/thirdparty.security.properties";


	public static void main(String[] args) {
		//SpringApplication.run(Application.class, args);

		new SpringApplicationBuilder(Application.class)
				.properties(PROPERTIES)
				.run(args);
	}
}
