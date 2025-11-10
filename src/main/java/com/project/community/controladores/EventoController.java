package com.project.community.controladores;


import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project.community.config.LocalDateTimeAdapter;
import com.project.community.entidades.Evento;
import com.project.community.servicios.EventoServiceImpl;
import com.project.community.servicios.ImageServiceImpl;

@RestController
@CrossOrigin(origins="http://localhost:4200")
@RequestMapping("/evento")
public class EventoController {
	
	private final EventoServiceImpl eventoService;
	private final ImageServiceImpl imageService;
	
	public EventoController(EventoServiceImpl eventoService, ImageServiceImpl imageService) {
		this.eventoService = eventoService;
		this.imageService = imageService;	
	}
	
	@GetMapping("{id}")
	public Evento getEvento(@PathVariable Long id) {
		return eventoService.getEvento(id);
	}
	
	@PostMapping(consumes = "multipart/form-data")
	public Evento createEvento(
			@RequestParam("evento") String eventoJson,
			@RequestParam(value="image", required=false) MultipartFile imagen 
			) {
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
				.create();
		Evento evento = gson.fromJson(eventoJson, Evento.class);
		if(imagen != null && !imagen.isEmpty()) {
			String archivo = imageService.postImage(imagen, evento.getId());
			evento.setImagenEvento(archivo);
		}
		return eventoService.postEvento(evento);
	}
	
	@PutMapping("modificar/{id}")
	public Evento updateEvento(@RequestBody Evento evento) {
		return eventoService.putEvento(evento);
	}
	
	@GetMapping
	public Iterable<Evento>getEventos(){
		return eventoService.getEventos();
	}
	
	@DeleteMapping("delete/{id}")
	public void deleteEvento(Evento evento) {
		eventoService.deleteEvento(evento);
	}
}
