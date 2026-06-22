package com.project.community.controladores;

import java.util.List;

import java.util.Set;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.community.DTO.ParticipanteAmigoDTO;
import com.project.community.DTO.ParticipanteDTO;
import com.project.community.servicios.ParticipanteService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/participantes/")
@CrossOrigin("http://localhost:4200")
public class ParticipanteController {

	private final ParticipanteService participanteService;
	
	@GetMapping("{id}")
	public ParticipanteDTO getParticipante(@PathVariable Long id) {
		return participanteService.getParticipanteDTO(id);
	}
	
	@GetMapping("amigos/{id}")
	public ParticipanteAmigoDTO getAmigo(@PathVariable Long id) {
		return participanteService.getAmigoDTO(id);
	}
	
	@GetMapping
	public List<ParticipanteDTO> getParticipantes(){
		return participanteService.getParticipantesDTO();
	}
	
	@GetMapping("amigos")
	public Set<ParticipanteAmigoDTO> getAmigos(){
		return participanteService.getAmigosAutenticadoDTO();
	}
	
	@GetMapping("amigos/mostrar")
	public Set<ParticipanteDTO> mostrarPosiblesAmigos(@RequestParam String input){
		return participanteService.mostrarListaAmigosDTO(input);
	}
	
	@PostMapping("amigos/add/{id}")
	public void addAmigo(@PathVariable("id") Long amigoId){
		participanteService.addAmigo(amigoId);
	}
	
	@PostMapping("imagen")
	public void cambiarImagen(@RequestPart (value="image") MultipartFile imagen) {

		participanteService.cambiarImagen(imagen);
	}
}