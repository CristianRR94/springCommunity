package com.project.community.controladores;

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
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.project.community.config.LocalDateTimeAdapter;
import com.project.community.entidades.Evento;
import com.project.community.mapper.EventoMapper;
import com.project.community.servicios.EventoServiceImpl;
import com.project.community.servicios.ImageServiceImpl;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins="http://localhost:4200")
@RequestMapping("/evento")
public class EventoController {
	
	private final EventoServiceImpl eventoService;
	private final ImageServiceImpl imageService;
	private final EventoMapper eventoMapper;
	
	public EventoController(EventoServiceImpl eventoService, ImageServiceImpl imageService, EventoMapper eventoMapper) {
		this.eventoService = eventoService;
		this.imageService = imageService;	
		this.eventoMapper = eventoMapper;
	}
	
//	@GetMapping("{id}")
//	public Evento getEvento(@PathVariable Long id) {
//		return eventoService.getEvento(id);
//	}
	
	@GetMapping("/{id}")
	public EventoDTO getEvento(@PathVariable Long id) {
		Evento evento = eventoService.getEvento(id);
		return eventoMapper.toDTO(evento);
	}
	
//	@PostMapping(consumes = "multipart/form-data")
//	public Evento createEvento(
//			@RequestPart("evento") Evento evento,
//			@RequestPart(value="image", required=false) MultipartFile imagen 
//			) {
//		if(imagen != null && !imagen.isEmpty()) {
//			String archivo = imageService.postImage(imagen);
//			evento.setImagenEvento(archivo);
//		}
//		return eventoService.postEvento(evento);
//	}
	
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
	
//	@PutMapping("modificar/{id}")
//	public Evento updateEvento(@RequestBody Evento evento) {
//		return eventoService.putEvento(evento);
//	}
	
	@PutMapping("modificar/{id}")
	public EventoDTO updateEvento(@Valid @RequestBody EventoDTO eventoDTO) {
		Evento evento = eventoMapper.toEvento(eventoDTO);
		Evento eventoGuardado = eventoService.putEvento(evento);
		return eventoMapper.toDTO(eventoGuardado);
	}
	
//	@GetMapping
//	public Iterable<Evento>getEventos(){
//		return eventoService.getEventos();
//	}
	
	@GetMapping
	public Iterable<EventoDTO> getEventos(){
		Iterable<Evento> eventos = eventoService.getEventos();
		return eventoMapper.toDTOs(eventos);
	}
	
//	@DeleteMapping("delete/{id}")
//	public void deleteEvento(Long id) {
//		eventoService.deleteEvento(id);
//	}
	
	@DeleteMapping("delete/{id}")
	public void deleteEvento(Long id) {
		eventoService.deleteEvento(id);
	}
}
