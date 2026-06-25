package com.project.community.controladores;

import java.util.List;



import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.project.community.DTO.EventoDTO;
import com.project.community.DTO.EventoPrincipalDTO;
import com.project.community.servicios.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins="http://localhost:4200")
@RequestMapping("/api/eventos")
public class EventoController {
	
	private final EventoService eventoService;	
	
	@GetMapping("/{id}")
	public EventoDTO getEvento(@PathVariable Long id) {
		return eventoService.getEventoDTO(id);
	}
	
	@GetMapping("/mis-eventos")
	public List<EventoPrincipalDTO> getEventosPorParticipanteId(){
		return eventoService.getEventosPorParticipantePrincipalDTO();
	}

	@PostMapping(consumes = "multipart/form-data")
	public EventoDTO createEvento(
			@Valid @RequestPart("evento") EventoDTO eventoDTO,
			@RequestPart(value="image", required=false) MultipartFile imagen
			) {
		
		return eventoService.createEventoDTO(eventoDTO, imagen);
		
	}


	@PutMapping(value = "/modificar/{id}", consumes="multipart/form-data" )
	public EventoDTO updateEvento(
			@Valid @RequestPart("evento") EventoDTO eventoDTO,
			@RequestPart(value="image", required=false) MultipartFile imagen,
			@PathVariable Long id) {

		return eventoService.putEvento(eventoDTO, imagen, id);

	}
	
	@GetMapping
	public List<EventoPrincipalDTO> getEventosPrincipalDTO(){
		return eventoService.getEventosPrincipalDTO();
		
	}

	@DeleteMapping("/delete/{id}")
	public void deleteEvento(@PathVariable Long id) {
		eventoService.deleteEvento(id);
	}

}
