package com.project.community.config;

import java.util.List;


import org.springframework.context.annotation.Bean;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//            .csrf(csrf -> csrf.disable())
//            .cors(cors->{})
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/api/login").permitAll()
//                .anyRequest().authenticated()
//            )
//            .formLogin(form -> form
//                .loginProcessingUrl("/api/login")
//                .successHandler((req, res, auth) -> res.setStatus(200))
//                .failureHandler((req, res, ex) -> res.setStatus(401))
//            )
//            .logout(logout -> logout
//                .logoutUrl("/api/logout")
//                .logoutSuccessHandler((req, res, auth) -> res.setStatus(200))
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID")
//            )
//            .build();
//    }
//    
//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//    	CorsConfiguration config = new CorsConfiguration();
//    	config.setAllowedOrigins(List.of("http://localhost:4200"));
//    	config.setAllowedMethods(List.of("GET", "POST", "DELETE", "PUT", "OPTIONS"));
//    	config.setAllowedHeaders(List.of("Content-Type", "Authorization"));
//    	config.setAllowCredentials(true);
//    	
//    	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//    	source.registerCorsConfiguration("/**", config);
//    	return source;
//    }
//
//    @Bean
//     PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
    
  //probar sin security
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
