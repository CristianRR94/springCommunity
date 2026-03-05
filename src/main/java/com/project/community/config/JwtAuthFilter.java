package com.project.community.config;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.community.entidades.Token;
import com.project.community.repositorios.TokenRepository;
import com.project.community.servicios.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	private final TokenRepository tokenRepository;

	
	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request, 
			@NonNull HttpServletResponse response, 
			@NonNull FilterChain filterChain
	) throws ServletException, IOException {
		if(request.getServletPath().contains("/auth")) { //comprobar
			filterChain.doFilter(request, response);
			return;
		}
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		final String jwtToken = authHeader.substring(7);
		final String userEmail = jwtService.extractUsername(jwtToken);
		if(userEmail == null || SecurityContextHolder.getContext().getAuthentication() != null) {
			filterChain.doFilter(request, response);
			return;
		}
		
		final Token token = tokenRepository.findByToken(jwtToken)
				.orElse(null);
		if(token == null || token.isExpired() || token.isRevoked()) {
			filterChain.doFilter(request, response);
			return;
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
//		final Optional<Usuario> usuario = usuarioRepository.findByEmail(userDetails.getUsername());
//		
//		if(usuario.isEmpty()) {
//			filterChain.doFilter(request, response);
//			return;
//		}
		final boolean isTokenValid = jwtService.isTokenValid(jwtToken, userDetails);
		if(!isTokenValid) {
			filterChain.doFilter(request, response);
			return;
		}
		
		
		final var authToken = new UsernamePasswordAuthenticationToken(
				userDetails, 
				null,
				userDetails.getAuthorities()
				);
		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authToken);
		
		filterChain.doFilter(request, response);
	}

}
