package com.project.community.controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.community.DTO.UsuarioEntradaDTO;
import com.project.community.entidades.Usuario;
import com.project.community.mapper.UsuarioMapper;
import com.project.community.servicios.UsuarioParticipanteService;
import com.project.community.servicios.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin("http://localhost:4200")
public class AuthController {
	//zona publica
	
	private final UsuarioMapper usuarioMapper;
	private final UsuarioParticipanteService usuarioParticipanteService;
	private final UsuarioService usuarioService;
	@PostMapping("/crear")
	public ResponseEntity<TokenResponse> postUsuario(@RequestBody @Valid UsuarioEntradaDTO usuarioDTO){
		Usuario usuarioEntrada = usuarioMapper.toUsuarioEntrada(usuarioDTO);
		TokenResponse token = usuarioParticipanteService.createUsuarioParticipante(usuarioEntrada);
		if(token == null) {
			return ResponseEntity.badRequest().build();
		}
	
		return ResponseEntity.ok(token);
		
	}
	
	//esto va de autenticar
	@PostMapping("/login")
	public ResponseEntity<TokenResponse> authenticate(@RequestBody @Valid UsuarioEntradaDTO usuarioDTO) {
		System.out.println("ENTRÓ AL LOGIN");
		Usuario usuarioEntrada = usuarioMapper.toUsuarioEntrada(usuarioDTO);
		final TokenResponse token = usuarioService.login(usuarioEntrada);
		return ResponseEntity.ok(token);
	}
	
	
}
