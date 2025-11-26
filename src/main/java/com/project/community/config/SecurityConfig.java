//package com.project.community.config;
//
//import com.project.community.security.AuthEntryPointJwt;
//import com.project.community.security.AuthTokenFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    private final AuthTokenFilter authTokenFilter;
//
//	private static final String WHITE_LIST_URL = "";
//    private final AuthEntryPointJwt authEntryPointJwt;
//
//    SecurityConfig(AuthEntryPointJwt authEntryPointJwt, AuthTokenFilter authTokenFilter) {
//        this.authEntryPointJwt = authEntryPointJwt;
//        this.authTokenFilter = authTokenFilter;
//    }
//
//	@Bean
//	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//		http.csrf(csrf->csrf.disable())
//		.cors(cors->cors.disable())
//		.authorizeHttpRequests(req -> req.requestMatchers(WHITE_LIST_URL)
//				.permitAll()
//				.anyRequest()
//				.authenticated())
//		.oauth2ResourceServer(Oauth2ResourceServerConfigurer::jwt)
//		.sessionManagement(session -> session.sessionCreationPoliciy(SessionCreationPolicy.STATELESS))
//		.httpBasic(withDefaults()).build();
//		return http.build();
//	}
//}
