package com.project.community.servicios;


import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.project.community.dominio.UsuarioNotFoundException;
import com.project.community.entidades.Participante;
import com.project.community.entidades.Usuario;
import com.project.community.repositorios.ParticipanteRepository;
import com.project.community.repositorios.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ParticipanteServiceImpl implements ParticipanteService{
	
	private final ParticipanteRepository participanteRepository;
	private final UsuarioRepository usuarioRepository; 
	@Override
	public Participante getParticipante(Long id) {
	
		return participanteRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Participante postParticipante(Participante participante) {
		
		return participanteRepository.save(participante);
	}

	@Override
	public List<Participante> getParticipantes() {
		
		return participanteRepository.findAll();
	}

	@Override
	@Transactional
	public Participante putParticipante(Participante participante) {
		
		return participanteRepository.save(participante);
	}

	@Override
	@Transactional
	public void deleteParticipante(Participante participante) {
		
		participanteRepository.delete(participante);
	}
	
	@Override
	public Participante findParticipanteByUsuario(Long id) {
		return participanteRepository.findByUsuarioId(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Participante findParticipanteWithAmigosByUsuarioId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String nombre = auth.getName();
		if(nombre == null || nombre.isBlank()) {
			throw new UsuarioNotFoundException("Nombre no encontrado");
		}
		Usuario usuario = usuarioRepository.findByNombre(nombre).orElseThrow(()-> new UsuarioNotFoundException("usuario no encontrado"));
		return participanteRepository.mostrarListaAmigosPorUsuario(usuario.getId())
				.orElseThrow(()-> new EntityNotFoundException("Usuario no encontrado"));
	}
	
	@Override
	@Transactional
	public void addAmigo(Long idAmigo) {
		Long idParticipante = obtenerParticipanteAutenticado().getId();
		Participante p = participanteRepository.findWithAmigosById(idParticipante).
				orElseThrow(()->new EntityNotFoundException("Usuario no encontrado"));
		Participante amigo = participanteRepository.findById(idAmigo)
				.orElseThrow(()->new EntityNotFoundException("Amigo no encontrado"));
		if (idParticipante.equals(idAmigo)) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puedes ser tu propio amigo");
	    }
		p.agregarAmigo(amigo);
		participanteRepository.save(p);
	}
	
	@Override
	@Transactional
	public Participante crearParticipanteNombreUsuario(String nombre, Usuario usuario) {
		return Participante.builder().nombreParticipante(nombre).usuario(usuario).build();
	}
	
	@Override
	@Transactional
	public void cambiarParticipanteNombre(String nombre, Long UsuarioId) {
		Participante participante = this.findParticipanteByUsuario(UsuarioId);
		 participante.cambiarNombreParticipante(nombre);
		 participanteRepository.save(participante);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Set<Participante> getAmigos(Long id){
		Participante participante = participanteRepository.findWithAmigosById(id)
		.orElseThrow(()-> new EntityNotFoundException("Usuario no encontrado"));
		
		return participante.getAmigos();
	}

	@Override
	@Transactional(readOnly = true)
	public Set<Participante> mostrarListaAmigos(String input, Long miId) {
		Set<Participante> participantes = participanteRepository.buscarAmigos(input, miId);
		return participantes;
	}

	@Override
	@Transactional
	public Participante obtenerParticipanteAutenticado() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String nombre = auth.getName();
		Usuario usuario = usuarioRepository.findByNombre(nombre).orElseThrow(()-> new UsuarioNotFoundException("Fallo en la autenticación"));
		
		Participante participante = participanteRepository.findByUsuarioId(usuario.getId());
		if(participante == null) {
			throw new EntityNotFoundException("participante no encontrado");
		}
		return participante;
	}
	
		

}
