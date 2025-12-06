package com.project.community.controladores;

import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.community.DTO.ParticipanteAmigoDTO;
import com.project.community.DTO.ParticipanteDTO;
import com.project.community.entidades.Participante;
import com.project.community.entidades.Usuario;
import com.project.community.mapper.ParticipanteMapper;
import com.project.community.repositorios.UsuarioRepository;
import com.project.community.servicios.ParticipanteService;


@RestController
@RequestMapping("participante")
@CrossOrigin("http://localhost:4200")
public class ParticipanteController {

	private ParticipanteService participanteService;
	private ParticipanteMapper participanteMapper;
	private UsuarioRepository usuarioRepository;
	
	public ParticipanteController(ParticipanteService participanteService,
			ParticipanteMapper participanteMapper, 
			UsuarioRepository usuarioRepository) {
		this.participanteService  = participanteService;
		this.participanteMapper = participanteMapper;
		this.usuarioRepository = usuarioRepository;
	}
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
	public List<ParticipanteDTO>getParticipantes(){
		List<Participante> participantes = participanteService.getParticipantes();
		return participanteMapper.toDTOs(participantes);
	}
	
	@GetMapping("amigos")
	public Set<ParticipanteAmigoDTO> getAmigos(){
		//obtener nombre de auth
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String nombre = auth.getName();
		Usuario usuario = usuarioRepository.findByNombre(nombre);
		Participante participante = participanteService.findParticipanteByUsuario(usuario.getId());
		Set<Participante> participantes = participante.getAmigos();
		return participanteMapper.toAmigoListDTO(participantes);
	}
}
