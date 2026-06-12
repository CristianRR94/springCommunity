package com.project.community.servicios;





import com.project.community.controladores.TokenResponse;
import com.project.community.entidades.Usuario;

public interface CuentaService {
	public TokenResponse createUsuarioParticipante (Usuario usuario);
	
	public void deleteUsuarioParticipante (Long id);
	
	public Usuario modNombreUsuarioParticipante( String nuevoNombre);
		
}
