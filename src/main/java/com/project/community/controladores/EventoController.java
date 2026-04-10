package com.project.community.controladores;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.community.DTO.EventoDTO;
import com.project.community.DTO.EventoPrincipalDTO;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.project.community.config.LocalDateTimeAdapter;
import com.project.community.entidades.Evento;
import com.project.community.entidades.Usuario;
import com.project.community.mapper.EventoMapper;
import com.project.community.servicios.EventoService;
import com.project.community.servicios.ImageService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins="http://localhost:4200")
@RequestMapping("/api/eventos")
public class EventoController {
	
	private final EventoService eventoService;
	private final ImageService imageService;
	private final EventoMapper eventoMapper;
	
	public EventoController(EventoService eventoService, ImageService imageService, EventoMapper eventoMapper) {
		this.eventoService = eventoService;
		this.imageService = imageService;	
		this.eventoMapper = eventoMapper;
	}
	
	@GetMapping("/{id}")
	public EventoDTO getEvento(@PathVariable Long id) {
		Evento evento = eventoService.getEvento(id);
		return eventoMapper.toDTO(evento);
	}
	
	@GetMapping("/mis-eventos")
	public List<EventoPrincipalDTO> getEventosPorParticipanteId(Authentication auth){
		System.out.println("lista de eventos");
		Usuario usuario = (Usuario) auth.getPrincipal();
		Long idParticipante = usuario.getParticipante().getId();
		List<Evento> eventos = eventoService.getEventosPorParticipanteId(idParticipante);
		return eventoMapper.toPrincipalDTOList(eventos);
	}

	@PostMapping(consumes = "multipart/form-data")
	public EventoDTO createEvento(
			@Valid @RequestPart("evento") EventoDTO eventoDTO,
			@RequestPart(value="image", required=false) MultipartFile imagen 
			) {
		if(imagen != null && !imagen.isEmpty()) {
			String archivo = imageService.postImage(imagen);
			eventoDTO.setImagenEvento(archivo);
		}
		Evento evento = eventoMapper.toEvento(eventoDTO);
		Evento eventoGuardado = eventoService.postEvento(evento); 
		return eventoMapper.toDTO(eventoGuardado);
	}

	@PutMapping("/modificar/{id}")
	public EventoDTO updateEvento(@Valid @RequestBody EventoDTO eventoDTO) {
		Evento evento = eventoMapper.toEvento(eventoDTO);
		Evento eventoGuardado = eventoService.putEvento(evento);
		return eventoMapper.toDTO(eventoGuardado);
	}
	
	@GetMapping
	public List<EventoPrincipalDTO> getEventos(){
		List<Evento> eventos = eventoService.getEventos();
		return eventoMapper.toPrincipalDTOList(eventos);
	}

	@DeleteMapping("/delete/{id}")
	public void deleteEvento(Long id) {
		eventoService.deleteEvento(id);
	}

}
