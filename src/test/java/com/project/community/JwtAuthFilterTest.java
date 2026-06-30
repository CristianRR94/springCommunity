package com.project.community;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.project.community.config.JwtAuthFilter;
import com.project.community.entidades.Token;
import com.project.community.enums.TipoToken;
import com.project.community.repositorios.TokenRepository;
import com.project.community.servicios.JwtProviderService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class JwtAuthFilterTest {

	@InjectMocks
	private JwtAuthFilter jwtAuthFilter;
	
	@Mock private JwtProviderService jwtProviderService;
	@Mock private UserDetailsService userDetailsService;
	@Mock private TokenRepository tokenRepository;
	
	//tambien mocks de servlets
	@Mock private HttpServletRequest request;
	@Mock private HttpServletResponse response;
	@Mock private FilterChain filterChain;
	
	@BeforeEach
	@AfterEach
	void cleanSecurityContext() {
		//vaciar contexto de spring
		SecurityContextHolder.clearContext();
	}
	
	@Test
	void doFilterInterval_ShouldAutenticate_WhenTokenIsValid() throws ServletException, IOException{
		//arrange
		String tokenString = "validToken";
		UserDetails usuario = new User("nombreValido", "passwordValido", Collections.emptyList());
		
		Token token = new Token();
		token.setExpired(false);
		token.setRevoked(false);
		token.setTipoUso(TipoToken.ACCESS);
		
		//simulacion HTTP
		when(request.getServletPath()).thenReturn("/api/eventos");
		when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + tokenString);
		
		//simulacion respuesta servicios
		when(jwtProviderService.extractUsername(tokenString)).thenReturn("Cristian");
        when(tokenRepository.findByToken(tokenString)).thenReturn(Optional.of(token));
        when(userDetailsService.loadUserByUsername("Cristian")).thenReturn(usuario);
        when(jwtProviderService.isTokenValid(tokenString, usuario)).thenReturn(true);
        
        //act
        jwtAuthFilter.doFilter(request, response, filterChain);
        
        //assert 
        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNotNull();
        assertThat(auth.getName()).isEqualTo("nombreValido");
        
        // verificar que el filtro deja pasar la peticion
        verify(filterChain, times(1)).doFilter(request, response);
	}
	
	@Test
    void doFilterInternal_ShouldNotAuthenticate_WhenAuthorizationHeaderIsMissing() throws ServletException, IOException {
        // ARRANGE
        when(request.getServletPath()).thenReturn("/api/eventos");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null); // No hay cabecera

        // ACT
        jwtAuthFilter.doFilter(request, response, filterChain);

        // ASSERT
        // El contexto debe seguir vacío
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        // La petición sigue su curso normal
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
