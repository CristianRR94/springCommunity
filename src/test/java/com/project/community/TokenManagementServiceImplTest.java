package com.project.community;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.community.controladores.TokenResponse;
import com.project.community.entidades.Token;
import com.project.community.entidades.Usuario;
import com.project.community.enums.TipoToken;
import com.project.community.repositorios.TokenRepository;
import com.project.community.repositorios.UsuarioRepository;
import com.project.community.servicios.JwtProviderService;
import com.project.community.servicios.TokenManagementServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TokenManagementServiceImplTest {

	@Mock
	private TokenRepository tokenRepository;
	@Mock
	private JwtProviderService jwtProviderService;
	@Mock
	private UsuarioRepository usuarioRepository;
	
	@InjectMocks
	private TokenManagementServiceImpl tokenManagementServiceImpl;
	
	//sad path
	@Test
	void refresh_ShouldThrowIllegalArgException_WhenHeaderNotStartWithBearer() {
		//arrange
		String badHeader = "MalToken";
		
		//act & assert
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, ()->{
			tokenManagementServiceImpl.refresh(badHeader);
		});
		assertEquals("Invalid bearer token", ex.getMessage());
	}
	
	@Test
	void refresh_ShouldThrowIllegalArgException_WhenTokenIsRevoked() {
		//arrange

		String refreshToken= "refresh_token";
		String authHeader = "Bearer " + refreshToken;
		String userName = "name";
		
		//Mock usuario
		var usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNombre(userName);
		
		Token tokenDB = Token.builder()
				.token(refreshToken)
				.expired(false)
				.revoked(true)
				.usuario(usuario)
				.build();
		
		when(jwtProviderService.extractUsername(refreshToken)).thenReturn(userName);
		when(jwtProviderService.extractType(anyString())).thenReturn(TipoToken.REFRESH_TYPE.getValue());
		when(usuarioRepository.findByNombre(userName)).thenReturn(Optional.of(usuario));
		when(jwtProviderService.isTokenValid(refreshToken, usuario)).thenReturn(true);
		when(tokenRepository.findByToken(refreshToken)).thenReturn(Optional.of(tokenDB));
		
		when(tokenRepository.findByToken(refreshToken)).thenReturn(Optional.of(tokenDB));
		
		//act & assert
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, ()->{
			tokenManagementServiceImpl.refresh(authHeader);
		});
		assertEquals("Invalid token", ex.getMessage());
	}
	
	//happy path
	@Test
	void refresh_ShouldReturnNewToken_WhenRefreshTokenIsValid() {
		
		//arrange
		String refreshToken= "refresh_token";
		String authHeader = "Bearer " + refreshToken;
		String userName = "name";
		
		//Mock usuario
		var usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNombre(userName);
		
		Token tokenDB = Token.builder()
				.token(refreshToken)
				.expired(false)
				.revoked(false)
				.build();
		
		//comprobaciones del service
		when(jwtProviderService.extractUsername(refreshToken)).thenReturn(userName);
		when(jwtProviderService.extractType(anyString())).thenReturn(TipoToken.REFRESH_TYPE.getValue());
		when(usuarioRepository.findByNombre(userName)).thenReturn(Optional.of(usuario));
		when(jwtProviderService.isTokenValid(refreshToken, usuario)).thenReturn(true);
		when(tokenRepository.findByToken(refreshToken)).thenReturn(Optional.of(tokenDB));
		
		//Mock generacion de tokens
		when(jwtProviderService.generateToken(usuario)).thenReturn("access_token");
		when(jwtProviderService.generateRefreshToken(usuario)).thenReturn("refresh_token");
		
		// Simular los métodos void internos (revocación y guardado)
        when(tokenRepository.findAllByUsuarioIdAndExpiredIsFalseAndRevokedIsFalse(usuario.getId()))
                .thenReturn(Collections.emptyList()); // Lista vacía para simplificar revokeAllUserTokens

        // Act
        TokenResponse response = tokenManagementServiceImpl.refresh(authHeader);

        // Assert
        assertNotNull(response);
        assertEquals("access_token", response.accessToken());
        assertEquals("refresh_token", response.refreshToken());

        // Verificamos que realmente se intentó persistir la información en la Base de Datos
        verify(tokenRepository, atLeastOnce()).save(any(Token.class));
	}
}
