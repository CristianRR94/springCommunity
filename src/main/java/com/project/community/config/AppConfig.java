package com.project.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.community.entidades.Usuario;
import com.project.community.repositorios.UsuarioRepository;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class AppConfig {
	
	private final UsuarioRepository usuarioRepo;
	
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
    	    			.roles(usuario.getRol()) //cuidado
    	    			.build();
    		
    	};
    }
    //para desencriptar password
    @Bean AuthenticationProvider authProvider(UserDetailsService userDetailsService) {
    	DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
    	authProvider.setPasswordEncoder(passwordEncoder());
    	return authProvider;
    }
}
