package com.example.marek;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.validation.Validator;
import java.util.Locale;


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