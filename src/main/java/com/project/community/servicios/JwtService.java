package com.project.community.servicios;


import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;

import com.project.community.entidades.Usuario;



public interface JwtService {
	public String generateToken(Usuario usuario);
	
	public String generateRefreshToken(final Usuario usuario);
	
	public String extractUsername(String token);
	
	public Date extractExpiration(String token);
	
	public boolean isTokenValid(final String token, final UserDetails userDetails);
	
//	public boolean isTokenExpired(final String token);
	
	public String extractType(final String token);

	Long extractId(String id);
	
	
}
