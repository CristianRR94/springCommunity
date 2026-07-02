package com.project.community.controller;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.community.DTO.UsuarioEntradaDTO;
import com.project.community.config.JwtAuthFilter;
import com.project.community.controladores.AuthController;
import com.project.community.controladores.TokenResponse;
import com.project.community.entidades.Usuario;
import com.project.community.mapper.UsuarioMapper;
import com.project.community.servicios.CuentaService;
import com.project.community.servicios.LoginService;
import com.project.community.servicios.TokenManagementService;
import com.project.community.storage.StorageProperties;

@WebMvcTest(
		controllers = AuthController.class,
		excludeFilters = @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, classes = JwtAuthFilter.class))
@AutoConfigureMockMvc(addFilters = false) 
public class AuthControllerTest {
	
	@Autowired
	private MockMvc mockmvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	
	@MockitoBean private UsuarioMapper usuarioMapper;
	@MockitoBean private CuentaService cuentaService;
	@MockitoBean private LoginService loginService;
	@MockitoBean private TokenManagementService tokenManagementService;
	
	@MockitoBean private StorageProperties storageProperties;

	//happy path
	@Test
	void postUsuario_ShouldReturnOkAndTokens_WhenPayloadIsCorrect() throws Exception {
		// arrange
		UsuarioEntradaDTO dto = new UsuarioEntradaDTO("nombreqqqqqq", "password123", "email@mail.com");
		Usuario mockUsuario = new Usuario();
		TokenResponse respuestaTokens = new TokenResponse("access_fake", "refresh_fake");
		
		when(usuarioMapper.toUsuarioEntrada(any(UsuarioEntradaDTO.class))).thenReturn(mockUsuario);
		when(cuentaService.createUsuarioParticipante(any(Usuario.class))).thenReturn(respuestaTokens);
		
		// act & assert
		mockmvc.perform(post("/auth/crear")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.access_token").value("access_fake"))
		.andExpect(jsonPath("$.refresh_token").value("refresh_fake"));
	}
	
	@Test
	void authenticate_ShouldReturnOkAndTokens_WhenPayloadIsCorrect() throws Exception{
		//arrange 
		UsuarioEntradaDTO dto = new UsuarioEntradaDTO("nombreqqqqqq", "password123", "email@mail.com");
		Usuario mockUsuario = new Usuario();
		TokenResponse respuestaTokens = new TokenResponse("access_fake", "refresh_fake");
		
		when(usuarioMapper.toUsuarioEntrada(any(UsuarioEntradaDTO.class))).thenReturn(mockUsuario);
		when(loginService.login(any(Usuario.class))).thenReturn(respuestaTokens);
		
		// act & assert
		mockmvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.access_token").value("access_fake"))
		.andExpect(jsonPath("$.refresh_token").value("refresh_fake"));
	}
	
	@Test
	void refreshToken_ShouldReturnOkAndRefreshToken() throws Exception {
		//arrange
		TokenResponse token = new TokenResponse("access_fake", "refresh_fake");
		String authHeader = "correctHeader";
		
		when(tokenManagementService.refresh(authHeader)).thenReturn(token);
		
		//act & assert
		mockmvc.perform(post("/auth/refresh")
				.header("Authorization", authHeader)
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.access_token").value("access_fake"))
		.andExpect(jsonPath("$.refresh_token").value("refresh_fake"));		
	}
	
	//sad path
	@Test
	void authenticate_ShouldReturnBadRequest_WhenPayloadIsInvalid() throws Exception {
		// arrange
		// 🚫 Enviamos un DTO con datos que sabes que van a romper tus anotaciones de validación (@Valid)
		UsuarioEntradaDTO dtoInvalido = new UsuarioEntradaDTO("", "", "email-mal-formado");
		
		// act & assert
		mockmvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dtoInvalido))) // Enviamos la pifia de JSON
		.andDo(print())
		.andExpect(status().isBadRequest()); // 🛑 Tu Handler debería capturarlo y devolver 400
	}
	
	@Test
	void refresh_ShouldReturnBadRequest_WhenAccessTokenIsInvalid() throws Exception {
		//arrange
		
		String authHeader = "badHeader";
		
		when(tokenManagementService.refresh(authHeader)).thenThrow(new io.jsonwebtoken.MalformedJwtException(authHeader));
		
		//act & assert
				mockmvc.perform(post("/auth/refresh")
						.header("Authorization", authHeader)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.status").value(401))
				.andExpect(jsonPath("$.mensaje").value("Token inválido"));	
	}
	

}