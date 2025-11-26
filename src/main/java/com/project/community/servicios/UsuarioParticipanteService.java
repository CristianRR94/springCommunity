package com.project.community.servicios;



import com.project.community.entidades.Usuario;

public interface UsuarioParticipanteService {
	public Usuario createUsuarioParticipante (Usuario usuario);
	
	public void deleteUsuarioParticipante (Long id);
	
	public Usuario modNombreUsuarioParticipante(Long id, String nuevoNombre);
}
