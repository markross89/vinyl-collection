package com.example.marek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.web.filter.CharacterEncodingFilter;


@SpringBootApplication
public class MarekApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarekApplication.class, args);
	}
}
