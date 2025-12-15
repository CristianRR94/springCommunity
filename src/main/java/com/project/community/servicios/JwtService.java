package com.project.community.servicios;

import com.project.community.entidades.Usuario;



public interface JwtService {
	public String generateToken(Usuario usuario);
	
	public String generateRefreshToken(final Usuario usuario);
	

}
