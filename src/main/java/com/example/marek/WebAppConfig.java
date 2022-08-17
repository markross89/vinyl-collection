package com.example.marek;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebAppConfig implements WebMvcConfigurer {
	
	@Override
	public void addViewControllers (ViewControllerRegistry registry) {
		
		registry.addViewController("/login").setViewName("/login/login");
		registry.addViewController("/contact").setViewName("/contact/contact");
		registry.addViewController("/about").setViewName("/contact/about");
		registry.addViewController("/tracklist/add").setViewName("/tracklist/add");
		registry.addViewController("/box/add").setViewName("/box/add");
	}
}