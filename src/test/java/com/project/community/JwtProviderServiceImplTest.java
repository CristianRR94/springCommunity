package com.project.community;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

import com.project.community.entidades.Usuario;
import com.project.community.enums.TipoToken;
import com.project.community.servicios.JwtProviderServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

@ExtendWith(MockitoExtension.class)
public class JwtProviderServiceImplTest {
	
	@Mock
	private Usuario mockUsuario;

	@InjectMocks
	private JwtProviderServiceImpl jwtProviderServiceImpl;
	
	//base64
	private final String secretKey = "bXktc3VwZXItc2VjcmV0LWtleS1mb3Itand0LWdlbmVyYXRpb24tcHVycG9zZXM=";
	
	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(jwtProviderServiceImpl, "secretKey", secretKey);
		ReflectionTestUtils.setField(jwtProviderServiceImpl, "jwtExpiration", 3600000L);
		ReflectionTestUtils.setField(jwtProviderServiceImpl, "refreshExpiration", 604800000L);
	}
	
	void createUsuario(Long id, String nombreMock) {
		//arrange
		when(mockUsuario.getId()).thenReturn(id);
		when(mockUsuario.getNombre()).thenReturn(nombreMock);
		when(mockUsuario.getAuthorities()).thenReturn(Collections.emptyList());
	}
	
	//happy path *****************************************************************************
	
	@Test
	void generateToken_ShouldReturnValidToken_WhenUsuarioIsValid() {
		//arrange
		createUsuario(1L, "nombreMock");
		
		//act
		String token = jwtProviderServiceImpl.generateToken(mockUsuario);
		
		//assert
		assertNotNull(token);
		assertEquals("nombreMock", jwtProviderServiceImpl.extractUsername(token));
		assertEquals(TipoToken.ACCESS.getValue(), jwtProviderServiceImpl.extractType(token));
		assertEquals(1L, jwtProviderServiceImpl.extractId(token));
	}
	
	@Test
	void generateRefreshToken_ShouldReturnRefreshToken_WhenUsuarioIsValid() {
		//arrange
		createUsuario(1L, "nombreRefreshMock");
		
		//act
		String refreshToken = jwtProviderServiceImpl.generateRefreshToken(mockUsuario);
		
		//assert
		assertNotNull(refreshToken);
		assertEquals("nombreRefreshMock", jwtProviderServiceImpl.extractUsername(refreshToken));
		assertEquals(1L, jwtProviderServiceImpl.extractId(refreshToken));
		assertEquals(TipoToken.REFRESH.getValue(), jwtProviderServiceImpl.extractType(refreshToken));
	}
	
	@Test
	void isTokenValid_ShouldReturnTrue_WhenUsernameMatchesTokenName() {
		//arrange
		createUsuario(1L, "nameMock");
		
		//act
		String token = jwtProviderServiceImpl.generateToken(mockUsuario);
		
		User usuario = new User("nameMock", "password", Collections.emptyList());
		
		boolean isValid = jwtProviderServiceImpl.isTokenValid(token, usuario);
		
		//assert
		Assertions.assertThat(isValid).isTrue();
	}
	
	// sad path *****************************************************************************
	
	@Test
	void isTokenValid_ShouldRetunrFalse_WhenUsernameNotTokenName() {
		//arrange
		createUsuario(1L, "nameMock");
		
		//act
		String token = jwtProviderServiceImpl.generateToken(mockUsuario);
		
		User usuario = new User("otroNombre", "password", Collections.emptyList());
		
		boolean isValid = jwtProviderServiceImpl.isTokenValid(token, usuario);
		
		//assert
		Assertions.assertThat(isValid).isFalse();
	}
	
	@Test
	void isTokenValid_ShouldThrowExpiredJwtException_WhenJwtIsExpired() {
		//arrange
		ReflectionTestUtils.setField(jwtProviderServiceImpl, "jwtExpiration", -1L);
		createUsuario(1L, "nameMock");
		String token = jwtProviderServiceImpl.generateToken(mockUsuario);
		User usuario = new User("nameMock", "password", Collections.emptyList());
		
		//act & assert
		Assertions.assertThatThrownBy(()->
		jwtProviderServiceImpl.isTokenValid(token, usuario))
		.isInstanceOf(ExpiredJwtException.class);
	}
	
	@Test
	void isTokenValid_ShouldThrowExpiredJwtException_WhenSignatureIsInvalid() {
		//arrange
		createUsuario(1L, "nameMock");
		String token = jwtProviderServiceImpl.generateToken(mockUsuario);
		String tokenMod = token + "modSignature";
		
		//act & assert
		Assertions.assertThatThrownBy(()->
		jwtProviderServiceImpl.extractUsername(tokenMod))
		.isInstanceOf(SignatureException.class);
	}
	
	@Test
	void extractUsername_ShouldThrowMalformedJwtException_WhenTokenIsNotAValidJwtStructure() {
	    // ARRANGE
	    String tokenMalformado = "un-texto-cualquiera-que-no-tiene-puntos";
	    
	    // ACT & ASSERT: La librería detectará que ni siquiera es un formato JWT válido
	    Assertions.assertThatThrownBy(() -> 
	        jwtProviderServiceImpl.extractUsername(tokenMalformado)
	    ).isInstanceOf(MalformedJwtException.class);
	}
}
