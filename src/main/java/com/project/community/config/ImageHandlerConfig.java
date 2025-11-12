package com.project.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImageHandlerConfig implements WebMvcConfigurer{
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registro) {
		registro.addResourceHandler("/images/**").addResourceLocations("file:" + System.getProperty("user.dir")+ "/images/");
	}
}
