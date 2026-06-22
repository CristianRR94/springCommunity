package com.project.community.servicios;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.community.controladores.TokenResponse;
import com.project.community.entidades.Token;
import com.project.community.entidades.Usuario;
import com.project.community.enums.TipoToken;
import com.project.community.repositorios.TokenRepository;
import com.project.community.repositorios.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class TokenManagementServiceImpl implements TokenManagementService{
	
	private final TokenRepository tokenRepository;
	private final JwtProviderService jwtProviderService;
	private final UsuarioRepository usuarioRepository;

	@Override
	public TokenResponse refresh(final String authHeader) {
		//empieza por berarer?
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new IllegalArgumentException("Ivalid bearer token");
		}
		
		//esta en la base de datos?
		final String refreshToken = authHeader.substring(7);
		final String userName = jwtProviderService.extractUsername(refreshToken);
		if(userName == null){
			throw new IllegalArgumentException("Invalid refresh token");
		}
		
		//tipo de token?
		final String tipo = jwtProviderService.extractType(refreshToken);
		if(!TipoToken.REFRESH_TYPE.getValue().equals(tipo)) {
			throw new IllegalArgumentException("Invalid token type");
		}
		
		final Usuario usuario = usuarioRepository.findByNombre(userName).orElseThrow(
				()->new UsernameNotFoundException(userName));
		if(!jwtProviderService.isTokenValid(refreshToken, usuario)) {
			throw new IllegalArgumentException("Invalid refresh token");
		}
		
		var tokenDB = tokenRepository.findByToken(refreshToken)
				.orElseThrow(()-> new IllegalArgumentException("Token no registrado"));
		if(tokenDB.isExpired() || tokenDB.isRevoked()) {
			throw new IllegalArgumentException("Invalid token");
		}
	
		final String accessToken = jwtProviderService.generateToken(usuario);
		final String newRefreshToken = jwtProviderService.generateRefreshToken(usuario);
		revokeAllUserTokens(usuario);
		saveUsuarioToken(usuario, accessToken);
		saveUsuarioToken(usuario, newRefreshToken);
		return new TokenResponse(accessToken, newRefreshToken);
	}
	
	
	@Override
	public void revokeAllUserTokens(final Usuario usuario) {
		final List<Token> validUserTokens = tokenRepository.findAllByUsuarioIdAndExpiredIsFalseAndRevokedIsFalse(usuario.getId());
		if(!validUserTokens.isEmpty()) {
			for(final Token token : validUserTokens) {
				token.setExpired(true);
				token.setRevoked(true);
			}
			tokenRepository.saveAll(validUserTokens);
		}
	}
	
	@Override
	public void saveUsuarioToken(Usuario usuario, String jwtToken) {
		String tipoUsoString = jwtProviderService.extractType(jwtToken);
		TipoToken tipoUsoEnum = TipoToken.fromValue(tipoUsoString);
		var token = Token.builder()
				.usuario(usuario)
				.token(jwtToken)
				.tokenType(Token.TokenType.BEARER)
				.expired(false)
				.revoked(false)
				.tipoUso(tipoUsoEnum)
				.build();
		tokenRepository.save(token);
	}

	@Override
	public void revokeAllTokensByToken(String token) {
		var tokenGuardado = tokenRepository.findByToken(token)
				.orElseThrow(()-> new RuntimeException("Token no encontrado"));
		revokeAllUserTokens(tokenGuardado.getUsuario());
		
	}
	
	

}
