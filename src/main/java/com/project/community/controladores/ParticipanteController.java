package com.project.community.controladores;

import java.util.List;

import java.util.Set;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.project.community.DTO.ParticipanteAmigoDTO;
import com.project.community.DTO.ParticipanteDTO;
import com.project.community.entidades.Participante;
import com.project.community.mapper.ParticipanteMapper;
import com.project.community.servicios.ParticipanteService;

import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
@RequestMapping("/api/participantes/")
@CrossOrigin("http://localhost:4200")
public class ParticipanteController {



	private final ParticipanteService participanteService;
	private final ParticipanteMapper participanteMapper;
	// private UsuarioRepository usuarioRepository;


	@Transactional
	@GetMapping("{id}")
	public ParticipanteDTO getParticipante(@PathVariable Long id) {
		Participante participante =  participanteService.getParticipante(id);
		return participanteMapper.toDTO(participante);
	}
	
	@GetMapping("amigos/{id}")
	public ParticipanteAmigoDTO getAmigo(@PathVariable Long id) {
		Participante participante =  participanteService.getParticipante(id);
		return participanteMapper.toAmigoDTO(participante);
	}
	
	@GetMapping
	public List<ParticipanteDTO> getParticipantes(){
		List<Participante> participantes = participanteService.getParticipantes();
		return participanteMapper.toDTOs(participantes);
	}
	
	//este es la muestra de amigos
	@GetMapping("amigos")
	public Set<ParticipanteAmigoDTO> getAmigos(){
		Participante participante = participanteService.findParticipanteWithAmigosByUsuarioId();
		Set<Participante> amigos = participante.getAmigos();
		return participanteMapper.toAmigoListDTO(amigos);
	}
	
	//esta es la lista de búsqueda de amigos
	@GetMapping("amigos/mostrar")
	public Set<ParticipanteDTO> mostrarPosiblesAmigos(@RequestParam String input ){
		Participante participante = participanteService.obtenerParticipanteAutenticado();
		Set<Participante> resultados = participanteService.mostrarListaAmigos(input, participante.getId());
		return participanteMapper.toSetParticipantesDTO(resultados);
	}
	
	@PostMapping("amigos/add/{id}")
	public void addAmigo(@PathVariable("id") Long amigoId){
		participanteService.addAmigo(amigoId);
	}
	

}
