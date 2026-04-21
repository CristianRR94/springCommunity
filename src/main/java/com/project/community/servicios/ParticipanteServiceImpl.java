package com.project.community.servicios;


import java.util.List;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.project.community.entidades.Participante;
import com.project.community.entidades.Usuario;
import com.project.community.repositorios.ParticipanteRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ParticipanteServiceImpl implements ParticipanteService{
	
	private final ParticipanteRepository participanteRepository;
	
	@Override
	public Participante getParticipante(Long id) {
	
		return participanteRepository.findById(id).orElse(null);
	}

	@Override
	public Participante postParticipante(Participante participante) {
		
		return participanteRepository.save(participante);
	}

	@Override
	public List<Participante> getParticipantes() {
		
		return participanteRepository.findAll();
	}

	@Override
	public Participante putParticipante(Participante participante) {
		
		return participanteRepository.save(participante);
	}

	@Override
	public void deleteParticipante(Participante participante) {
		
		participanteRepository.delete(participante);
	}
	
	@Override
	public Participante findParticipanteByUsuario(Long id) {
		return participanteRepository.findByUsuarioId(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Participante findParticipanteWithAmigosByUsuarioId(Long id) {
		return participanteRepository.mostrarListaAmigosPorUsuario(id)
				.orElseThrow(()-> new EntityNotFoundException("Usuario no encontrado"));
	}
	
	@Transactional
	public void addAmigo(Long idParticipante, Long idAmigo) {
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
	public Participante crearParticipanteNombreUsuario(String nombre, Usuario usuario) {
		return Participante.builder().nombreParticipante(nombre).usuario(usuario).build();
	}
	
	public void cambiarParticipanteNombre(String nombre, Long UsuarioId) {
		Participante participante = this.findParticipanteByUsuario(UsuarioId);
		 participante.cambiarNombreParticipante(nombre);
		 participanteRepository.save(participante);
	}
	
	
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
	
		

}
