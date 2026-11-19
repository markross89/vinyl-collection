
package com.example.marek.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver resolver = new SessionLocaleResolver();
		resolver.setDefaultLocale(Locale.ENGLISH);
		return resolver;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor interceptor =
				new LocaleChangeInterceptor();

		interceptor.setParamName("lang");

		return interceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {

		registry.addViewController("/login")
				.setViewName("/login/login");

		registry.addViewController("/contact")
				.setViewName("/contact/contact");

		registry.addViewController("/about")
				.setViewName("/contact/about");

		registry.addViewController("/tracklist/add")
				.setViewName("/tracklist/add");

		registry.addViewController("/box/add")
				.setViewName("/box/add");

		registry.addViewController("/403")
				.setViewName("/login/403");
	}
}
