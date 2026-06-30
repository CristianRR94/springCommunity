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
	
	private Usuario configurarMockUsuario(String token, String username, Token tokenDB) {
		var usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNombre(username);
		
		when(jwtProviderService.extractUsername(token)).thenReturn(username);
		when(jwtProviderService.extractType(anyString())).thenReturn(TipoToken.REFRESH.getValue());
		when(usuarioRepository.findByNombre(username)).thenReturn(Optional.of(usuario));
		when(jwtProviderService.isTokenValid(token, usuario)).thenReturn(true);
		when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenDB));
		
		return usuario;
	}
	
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
		// Arrange
				String refreshToken = "refresh_token";
				Token tokenDB = Token.builder().token(refreshToken).expired(false).revoked(true).build();
				
				configurarMockUsuario(refreshToken, "name", tokenDB); 
				
				// Act & Assert
				IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
					tokenManagementServiceImpl.refresh("Bearer " + refreshToken);
				});
				assertEquals("Invalid token", ex.getMessage());
	}
	
	//happy path
	@Test
	void refresh_ShouldReturnNewToken_WhenRefreshTokenIsValid() {
		
		// Arrange
				String refreshToken = "refresh_token";
				Token tokenDB = Token.builder().token(refreshToken).expired(false).revoked(false).build();
				
				Usuario usuario = configurarMockUsuario(refreshToken, "name", tokenDB); // <-- ¡Línea mágica!
				
				when(jwtProviderService.generateToken(usuario)).thenReturn("access_token");
				when(jwtProviderService.generateRefreshToken(usuario)).thenReturn("refresh_token");
		        when(tokenRepository.findAllByUsuarioIdAndExpiredIsFalseAndRevokedIsFalse(usuario.getId()))
		                .thenReturn(Collections.emptyList());

		        // Act
		        TokenResponse response = tokenManagementServiceImpl.refresh("Bearer " + refreshToken);

		        // Assert
		        assertNotNull(response);
		        assertEquals("access_token", response.accessToken());
		        assertEquals("refresh_token", response.refreshToken());
		        verify(tokenRepository, atLeastOnce()).save(any(Token.class));
	}
}
