package com.project.community.controladores;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
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
import com.project.community.entidades.Evento;
import com.project.community.enums.StorageFolder;
import com.project.community.mapper.EventoMapper;
import com.project.community.servicios.AuthDataService;
import com.project.community.servicios.EventoService;
import com.project.community.servicios.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins="http://localhost:4200")
@RequestMapping("/api/eventos")
public class EventoController {
	
	private final EventoService eventoService;
	private final ImageService imageService;
	private final EventoMapper eventoMapper;
	private final AuthDataService authDataService;
	
	
	
	@GetMapping("/{id}")
	public EventoDTO getEvento(@PathVariable Long id) {
		Evento evento = eventoService.getEvento(id);
		return eventoMapper.toDTO(evento);
	}
	
	@Transactional
	@GetMapping("/mis-eventos")
	public List<EventoPrincipalDTO> getEventosPorParticipanteId(){
		System.out.println("lista de eventos");
		Long idParticipante = authDataService.obtenerParticipanteAutenticado().getId();
		List<Evento> eventos = eventoService.getEventosPorParticipanteId(idParticipante);
		return eventoMapper.toPrincipalDTOList(eventos);
	}

	@Transactional
	@PostMapping(consumes = "multipart/form-data")
	public EventoDTO createEvento(
			@Valid @RequestPart("evento") EventoDTO eventoDTO,
			@RequestPart(value="image", required=false) MultipartFile imagen
			) {
		if(imagen != null && !imagen.isEmpty()) {
			String archivo = imageService.postImage(imagen, StorageFolder.EVENTOS);
			eventoDTO.setImagenEvento(archivo);
		}
		Evento eventoGuardado = eventoService.postEvento(eventoDTO);
		return eventoMapper.toDTO(eventoGuardado);
	}

	@Transactional
	@PutMapping(value = "/modificar/{id}", consumes="multipart/form-data" )
	public EventoDTO updateEvento(
			@Valid @RequestPart("evento") EventoDTO eventoDTO,
			@RequestPart(value="image", required=false) MultipartFile imagen,
			@PathVariable Long id) {
		Long idParticipante = authDataService.obtenerParticipanteAutenticado().getId();
		if(imagen != null && !imagen.isEmpty()) {
			String archivo = imageService.postImage(imagen, StorageFolder.EVENTOS);
			eventoDTO.setImagenEvento(archivo);
		}
		eventoDTO.setId(id);
		
		Evento eventoGuardado = eventoService.putEvento(eventoDTO, idParticipante);
		return eventoMapper.toDTO(eventoGuardado);
		
	}
	
	@GetMapping
	public List<EventoPrincipalDTO> getEventos(){
		List<Evento> eventos = eventoService.getEventos();
		return eventoMapper.toPrincipalDTOList(eventos);
	}

	@DeleteMapping("/delete/{id}")
	public void deleteEvento(@PathVariable Long id) {
		Long idParticipanteAutenticado = authDataService.obtenerParticipanteAutenticado().getId();
		eventoService.deleteEvento(id, idParticipanteAutenticado);
	}

}
