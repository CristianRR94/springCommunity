package com.project.community.config;

import java.util.List;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.project.community.entidades.Token;
import com.project.community.repositorios.TokenRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	

	private final JwtAuthFilter jwtAuthFilter;
	private final TokenRepository tokenRepository;
	private final AuthenticationProvider authProvider;
    
    //permitir peticiones
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
    	CorsConfiguration config = new CorsConfiguration();
    	config.setAllowedOriginPatterns(List.of("http://localhost:4200"));
    	config.setAllowedMethods(List.of("GET", "POST", "DELETE", "PUT", "OPTIONS"));
    	config.setAllowedHeaders(List.of("Content-Type", "Authorization"));
    	config.setAllowCredentials(true);
    	
    	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    	source.registerCorsConfiguration("/**", config);  
    	return source;
    }
       
    @Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
		.csrf(csrf->csrf.disable())
		.cors(cors->cors.configurationSource(corsConfigurationSource()))
		.authorizeHttpRequests(req -> req.requestMatchers("/auth/**")
				.permitAll()
				.requestMatchers("/api/usuarios/**", "/images/**")
				.permitAll()
				.requestMatchers("/ws-chat")
				.permitAll()
				.requestMatchers("/api/eventos/**", "/api/participantes/**")
				.authenticated()
				.anyRequest()
				.authenticated()
				)
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authenticationProvider(authProvider)
		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
		.logout(logout->
			logout.logoutUrl("/auth/logout")
			.addLogoutHandler((request, response, authentication)->{
				final var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
				logout(authHeader);
			})
		.logoutSuccessHandler((request, response, authentication)->
		SecurityContextHolder.clearContext())
		);
		return http.build();
	}
    
    @Transactional
    public void logout(final String token) {
    	if(token == null || !token.startsWith("Bearer ")) {
    		throw new IllegalArgumentException("Invalid token");
    	}
    	final String jwtToken = token.substring(7);
    	final Token foundToken = tokenRepository.findByToken(jwtToken)
    			.orElseThrow(()-> new IllegalArgumentException("Invalid token"));
    	List<Token> tokens = tokenRepository
    			.findAllByUsuarioIdAndExpiredIsFalseAndRevokedIsFalse(foundToken.getUsuario().getId());
    	for(final Token tokenValid : tokens) {
    		tokenValid.setExpired(true);
    		tokenValid.setRevoked(true);
    	}
    	tokenRepository.saveAll(tokens);
    }
 
}
