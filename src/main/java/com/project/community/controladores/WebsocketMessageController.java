package com.project.community.controladores;


import java.security.Principal;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.project.community.DTO.HistorialMensajesDTO;
import com.project.community.DTO.MensajeDTO;
import com.project.community.servicios.MensajeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;




@Controller
@RequiredArgsConstructor
public class WebsocketMessageController {
	private final MensajeService mensajeService;
	@MessageMapping("/chat/{eventoId}")
	@SendTo("/topic/evento/{eventoId}")
	public HistorialMensajesDTO getMensaje(@DestinationVariable Long eventoId, @Valid MensajeDTO mensaje, Principal principal) {
		String username = principal.getName();
		
		System.out.println("El mensaje es: " + mensaje + " para el evento con id: " + eventoId);
		HistorialMensajesDTO mensajeProcesado = mensajeService.guardarMensaje(eventoId, mensaje, username);
		return mensajeProcesado;
	}
}
