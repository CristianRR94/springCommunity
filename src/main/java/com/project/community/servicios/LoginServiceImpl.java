package com.project.community.servicios;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.community.controladores.TokenResponse;
import com.project.community.entidades.Usuario;
import com.project.community.repositorios.UsuarioRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

	private final UsuarioRepository usuarioRepository;
	private final JwtProviderService jwtProviderService;
	private final AuthenticationManager authManager;
	private final TokenManagementService tokenManagementService;
	
	@Override
	public TokenResponse login(Usuario usuario) {
		authManager.authenticate(
				new UsernamePasswordAuthenticationToken(usuario.getNombre(), usuario.getPassword()));
		Usuario usuarioLogin = usuarioRepository.findByNombre(usuario.getNombre()).orElseThrow(()->
				new UsernameNotFoundException("Usuario no encontrado"));
		String jwtToken = jwtProviderService.generateToken(usuarioLogin);
		String refreshToken = jwtProviderService.generateRefreshToken(usuarioLogin);
		tokenManagementService.revokeAllUserTokens(usuarioLogin);	
		tokenManagementService.saveUsuarioToken(usuarioLogin, jwtToken);
		tokenManagementService.saveUsuarioToken(usuarioLogin, refreshToken);
		return new TokenResponse(jwtToken, refreshToken);
	}

}
