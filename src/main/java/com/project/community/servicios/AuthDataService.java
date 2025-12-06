package com.project.community.servicios;

import com.project.community.entidades.Participante;
import com.project.community.entidades.Usuario;

public interface AuthDataService {

	public Usuario obtenerUsuarioAutenticado();
	
	public Participante obtenerParticipanteAutenticado();
}
