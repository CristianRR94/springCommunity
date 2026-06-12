package com.project.community.servicios;

import com.project.community.controladores.TokenResponse;
import com.project.community.entidades.Usuario;

public interface TokenManagementService {

	
	public TokenResponse refresh(final String authHeader);

	void saveUsuarioToken(Usuario usuario, String jwtToken);

	void revokeAllUserTokens(Usuario usuario);
	
	void revokeAllTokensByToken(String token);
}
