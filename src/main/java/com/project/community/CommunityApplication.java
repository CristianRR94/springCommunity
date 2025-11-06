package com.project.community;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.project.community.storage.StorageProperties;

@SpringBootApplication
//esto es para el date automático
@EnableJpaAuditing
@EnableConfigurationProperties(StorageProperties.class)
public class CommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
	}

}
