package com.project.community.servicios;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.community.entidades.Participante;
import com.project.community.entidades.Usuario;
import com.project.community.repositorios.UsuarioRepository;

@Service
public class AuthDataServiceImpl implements AuthDataService{
	
	private final UsuarioRepository usuarioRepository;
	private final ParticipanteService participanteService;
	
	public AuthDataServiceImpl(UsuarioRepository usuarioRepository, ParticipanteService participanteService) {
		this.usuarioRepository = usuarioRepository;
		this.participanteService = participanteService;
	}
public Usuario obtenerUsuarioAutenticado() {
	//obtener nombre de auth
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	if(auth == null) {
		throw new IllegalStateException("No hay usuario autenticado");
	}
	String email = auth.getName(); //devuelve email
	Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("No existe el email: " + email));
	return usuario;
}


public Participante obtenerParticipanteAutenticado() {
	
	
	Usuario usuario = obtenerUsuarioAutenticado();
	Participante participante = participanteService.findParticipanteByUsuario(usuario.getId());
	if(participante == null) {
		throw new IllegalStateException("No hay usuario autenticado");
	}
	return participante;
}
}
