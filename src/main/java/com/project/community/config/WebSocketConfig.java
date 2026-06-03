package com.project.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.project.community.interceptores.JwtChannelInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
	
	private final JwtChannelInterceptor jwtChannelInterceptor;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registro) {
		// Prefijo para los mensajes que el cliente ESCUCHA (Suscripciones)
		registro.enableSimpleBroker("/topic");
		// Prefijo para los mensajes que el cliente ENVÍA hacia nuestro servidor
		registro.setApplicationDestinationPrefixes("/app");
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registro) {
		// Este es el punto de conexión que usará el cliente para conectar el "cable"
		registro.addEndpoint("/ws-chat")
		.setAllowedOrigins("http://localhost:4200");

	}
	
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		 WebSocketMessageBrokerConfigurer.super.configureClientInboundChannel(registration);
		 
		 registration.interceptors(jwtChannelInterceptor);
	}
}
