package com.project.community.servicios;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.community.controladores.TokenResponse;
import com.project.community.entidades.Token;
import com.project.community.entidades.Usuario;
import com.project.community.repositorios.TokenRepository;
import com.project.community.repositorios.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService{

    private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authManager;
	private final UsuarioRepository usuarioRepository;
	private final JwtService jwtService;
	private final TokenRepository tokenRepository;

	
	

	@Override
	public Usuario getUsuario(Long id) {
		
		return usuarioRepository.findById(id).orElse(null);
	}

	@Override
	public List<Usuario> getUsuarios() {
		
		return usuarioRepository.findAll();
	}

	@Override
	public Usuario postUsuario(Usuario usuario) {
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		return usuarioRepository.save(usuario);
	}

	@Override
	public void deleteUsuario(Long id) {
		usuarioRepository.deleteById(id);
		
	}

	@Override
	public Usuario putUsuario(Usuario usuario) {
		
		return usuarioRepository.save(usuario);
	}

	@Override
	public Usuario encontrarNombre(String nombre) {
		
		return usuarioRepository.findByNombre(nombre);
	}

	@Override
	public TokenResponse login(Usuario usuario) {
		authManager.authenticate(
				new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getPassword()));
		Usuario usuarioLogin = usuarioRepository.findByEmail(usuario.getEmail()).orElseThrow(()->
				new UsernameNotFoundException("Usuario no encontrado"));
		String jwtToken = jwtService.generateToken(usuarioLogin);
		String refreshToken = jwtService.generateRefreshToken(usuarioLogin);
		revokeAllUserTokens(usuarioLogin);
		
		saveUsuarioToken(usuarioLogin, jwtToken);
		saveUsuarioToken(usuarioLogin, refreshToken);
		return new TokenResponse(jwtToken, refreshToken);
	}
	
	@Override
	@Transactional
	public void logout(String token) {
		var tokenGuardado = tokenRepository.findByToken(token)
				.orElseThrow(()-> new RuntimeException("Token no encontrado"));
		tokenGuardado.setExpired(true);
		tokenGuardado.setRevoked(true);
	}
	
	private void saveUsuarioToken(Usuario usuario, String jwtToken) {
		var token = Token.builder()
				.usuario(usuario)
				.token(jwtToken)
				.tokenType(Token.TokenType.BEARER)
				.expired(false)
				.revoked(false)
				.build();
		tokenRepository.save(token);
	}
	
	@Override
	public TokenResponse refresh(final String authHeader) {
		//empieza por berarer?
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new IllegalArgumentException("Ivalid bearer token");
		}
		
		//esta en la base de datos?
		final String refreshToken = authHeader.substring(7);
		final String userEmail = jwtService.extractUsername(refreshToken);
		if(userEmail == null){
			throw new IllegalArgumentException("Invalid refresh token");
		}
		
		final String tipo = jwtService.extractType(refreshToken);
		if(!"REFRESH".equals(tipo)) {
			throw new IllegalArgumentException("Invalid token type");
		}
		
		final Usuario usuario = usuarioRepository.findByEmail(userEmail).orElseThrow(
				()->new UsernameNotFoundException(userEmail));
		if(!jwtService.isTokenValid(refreshToken, usuario)) {
			throw new IllegalArgumentException("Invalid refresh token");
		}
		
		var tokenDB = tokenRepository.findByToken(refreshToken)
				.orElseThrow(()-> new IllegalArgumentException("Token no registrado"));
		if(tokenDB.isExpired() || tokenDB.isRevoked()) {
			throw new IllegalArgumentException("Invalid token");
		}
	
		final String accessToken = jwtService.generateToken(usuario);
		final String newRefreshToken = jwtService.generateRefreshToken(usuario);
		revokeAllUserTokens(usuario);
		saveUsuarioToken(usuario, accessToken);
		saveUsuarioToken(usuario, newRefreshToken);
		return new TokenResponse(accessToken, newRefreshToken);
	}

	private void revokeAllUserTokens(final Usuario usuario) {
		final List<Token> validUserTokens = tokenRepository.findAllByUsuarioIdAndExpiredIsFalseAndRevokedIsFalse(usuario.getId());
		if(!validUserTokens.isEmpty()) {
			for(final Token token : validUserTokens) {
				token.setExpired(true);
				token.setRevoked(true);
			}
			tokenRepository.saveAll(validUserTokens);
		}
	}



}
