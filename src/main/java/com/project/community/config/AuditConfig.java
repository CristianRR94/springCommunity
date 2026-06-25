package com.project.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
//esto es para el date automático
@EnableJpaAuditing
public class AuditConfig {

}
