package com.project.community.controladores;

import java.util.List;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.project.community.DTO.ParticipanteAmigoDTO;
import com.project.community.DTO.ParticipanteDTO;
import com.project.community.entidades.Participante;
import com.project.community.entidades.Usuario;
import com.project.community.mapper.ParticipanteMapper;
import com.project.community.repositorios.UsuarioRepository;
import com.project.community.servicios.ParticipanteService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
@RequestMapping("/api/participantes/")
@CrossOrigin("http://localhost:4200")
public class ParticipanteController {



	private ParticipanteService participanteService;
	private ParticipanteMapper participanteMapper;
	private UsuarioRepository usuarioRepository;


	
	@GetMapping("{id}")
	public ParticipanteDTO getParticipante(Long id) {
		Participante participante =  participanteService.getParticipante(id);
		return participanteMapper.toDTO(participante);
	}
	
	@GetMapping("amigos/{id}")
	public ParticipanteAmigoDTO getAmigo(Long id) {
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
		//obtener nombre de auth
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//el getName da el email
		String nombre = auth.getName();
		Usuario usuario = usuarioRepository.findByEmail(nombre)
				.orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
		Participante participante = participanteService.findParticipanteWithAmigosByUsuarioId(usuario.getId());
		Set<Participante> participantes = participante.getAmigos();
		return participanteMapper.toAmigoListDTO(participantes);
	}
	
	//esta es la lista de búsqueda de amigos
	@GetMapping("amigos/mostrar")
	public Set<ParticipanteDTO> mostrarPosiblesAmigos(@RequestParam @Valid String input ){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String nombre = auth.getName();
		Usuario usuario = usuarioRepository.findByEmail(nombre)
				.orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
		Long id = usuario.getId();
		String myInput = input;
		Set<Participante> resultados = participanteService.mostrarListaAmigos(myInput, id);
		return participanteMapper.toSetParticipantesDTO(resultados);
	}
	
	@PostMapping("amigos/add/{id}")
	public void addAmigo(@PathVariable("id") Long amigoId){
		Usuario u = autenticar();
		Participante p = participanteService.findParticipanteByUsuario(u.getId());	
		participanteService.addAmigo(p.getId(), amigoId);
	}
	
	private Usuario autenticar() {
		//obtener nombre de auth
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				//el getName da el email
				String nombre = auth.getName();
				Usuario usuario = usuarioRepository.findByEmail(nombre)
						.orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
				return usuario;
	}
}
