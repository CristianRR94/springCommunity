package com.project.community.interceptores;

import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import com.project.community.servicios.JwtService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor{
	private final JwtService jwtService;
	//metodo para modificar el mensaje antes de enviarlo, si emite null, no se envía
	public Message<?> preSend(Message<?> message, MessageChannel channel){
		//permite leer cabeceras de protocolos STOMP
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		//validación solo para connect
		if(accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
			String authHeader = accessor.getFirstNativeHeader("Authorization");
			//detección de token
			if(authHeader != null && authHeader.startsWith("Bearer ")) {
				String token = authHeader.substring(7);
				try {
					String usuario = jwtService.extractUsername(token);
					if(usuario != null) {
					// vinculación de la identidad del usuario al websocket 
					//e inyeccion de Principal en el controlador
						UsernamePasswordAuthenticationToken authentication =
								new UsernamePasswordAuthenticationToken(usuario, null , List.of());
						accessor.setUser(authentication);
					}
				} catch(Exception ex) {
					System.out.println("Error en la autenticación de websocket" + ex.getMessage());
				}
			}
		}
		return message;
	}
}
