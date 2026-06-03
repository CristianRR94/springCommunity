package com.project.community.controladores;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.community.DTO.HistorialMensajesDTO;
import com.project.community.servicios.MensajeService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
public class MensajeController {
	private final MensajeService mensajeService;
	
	@GetMapping("/{eventoId}/mensajes")
	public ResponseEntity<List<HistorialMensajesDTO>> getHistorialChat(@PathVariable Long eventoId){
		List<HistorialMensajesDTO> historial = mensajeService.obtenerHistorial(eventoId);
		return ResponseEntity.ok(historial);
	}
}
