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
	public Usuario createUsuarioParticipante(Usuario usuario) {
		Usuario nuevoUsuario = usuarioService.postUsuario(usuario);
		Participante participante = participanteService.crearParticipanteNombre(nuevoUsuario.getNombre());
		participanteService.postParticipante(participante);	
		return nuevoUsuario;		
	}

	@Override
	@Transactional
	public void deleteUsuarioParticipante(Long id){
		Participante participante = participanteService.findParticipanteByUsuario(id);
		if(participante !=null) {
			participanteService.deleteParticipante(participante);
		}
		usuarioService.deleteUsuario(id);
	}
	
	@Override
	@Transactional
	public Usuario modNombreUsuarioParticipante(Long id, String nuevoNombre) {
		Usuario usuario = usuarioService.getUsuario(id);
		if(nuevoNombre == null || nuevoNombre.isBlank()) {
			throw new IllegalArgumentException("Intoduce un nombre válido");
		}
		if(usuario == null) {
			throw new IllegalArgumentException("No se ha encontrado ningún usuario");
		}
		String nombreViejo = usuario.getNombre();
		if(nombreViejo == null || nombreViejo.isBlank()) {
			throw new IllegalArgumentException("No se ha encontrado ningún nombre");
		}
		if(nombreViejo.equals(nuevoNombre)) {
			throw new IllegalArgumentException("Introduce un nombre distinto al antiguo");
		}
		Usuario otroUsuario = usuarioService.encontrarNombre(nuevoNombre);
		if(otroUsuario.getNombre() != null) {
			throw new IllegalArgumentException("Nombre ya en uso");
		}
		participanteService.cambiarParticipanteNombre(nuevoNombre, id);
		usuario.cambiarNombre(nuevoNombre);
		usuarioService.putUsuario(usuario);
		return usuario;
		}
}
