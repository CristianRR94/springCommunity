package com.project.community.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.project.community.entidades.Usuario;
import com.project.community.repositorios.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	
	private final UsuarioRepository usuarioRepo;
    
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
     AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
     PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
     UserDetailsService userDetailsService() {
    	return username -> {
    		final Usuario usuario = usuarioRepo.findByEmail(username)
    				.orElseThrow(()->
    		new UsernameNotFoundException("User not found"));
    			return User.builder()
    	    			.username(usuario.getEmail())
    	    			.password(usuario.getPassword())
    	    			.build();
    		
    	};
    }
    //para desencriptar password
    @Bean AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
    	DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
    	authProvider.setPasswordEncoder(passwordEncoder());
    	return authProvider;
    }
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
