package com.project.community.config;

import java.nio.file.Path;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.project.community.storage.StorageProperties;

@Configuration
public class ImageHandlerConfig implements WebMvcConfigurer{
	
	private final StorageProperties properties;
	
	public ImageHandlerConfig(StorageProperties properties) {
		this.properties = properties;
	}
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registro) {
		if(properties == null || properties.getLocation() == null) {
			return;
		}
		String location = properties.getLocation();
		Path path = Path.of(location);
		String finalLocation = path.isAbsolute() ? 
				path.toUri().toString() : 
					"file:" + System.getProperty("user.dir") + "/" + location + "/";
		registro.addResourceHandler("/images/**")
		.addResourceLocations(finalLocation);
	}
}
