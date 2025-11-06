package com.project.community.servicios;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.project.community.entidades.Participante;
import com.project.community.entidades.Usuario;




@Service
public class UsuarioParticipanteServiceImpl implements UsuarioParticipanteService{


	private final UsuarioService usuarioService;
	private final ParticipanteService participanteService;
	
	public UsuarioParticipanteServiceImpl(UsuarioService usuarioService, ParticipanteService participanteService) {
		this.usuarioService = usuarioService;
		this.participanteService = participanteService;
	}

	@Override
	@Transactional
	public Usuario createUsuarioParticipante(Usuario usuario, Participante participante) {
		Usuario nuevoUsuario = usuarioService.postUsuario(usuario);
		participante.setNombreParticipante(nuevoUsuario.getNombre());
		participante.setUsuario(nuevoUsuario);
		participanteService.postParticipante(participante);
		
		return nuevoUsuario;
	}
	


}
