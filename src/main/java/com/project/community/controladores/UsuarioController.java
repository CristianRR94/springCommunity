package com.project.community.controladores;

import java.util.List;


import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.project.community.DTO.UsuarioEntradaDTO;
import com.project.community.DTO.UsuarioSalidaDTO;
import com.project.community.entidades.Usuario;
import com.project.community.mapper.UsuarioMapper;
import com.project.community.servicios.UsuarioParticipanteService;
import com.project.community.servicios.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {
	

	private final UsuarioService usuarioService;
	private final UsuarioParticipanteService usuarioParticipanteService;
	private final UsuarioMapper usuarioMapper;
	
	

	
	@GetMapping
	public List<UsuarioSalidaDTO> getUsuarios(){
	List<Usuario> usuarios = usuarioService.getUsuarios();
	return usuarioMapper.toSalidaDTOs(usuarios);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<UsuarioSalidaDTO> getUsuario(@PathVariable Long id) {
		Usuario usuario = usuarioService.getUsuario(id);
		 if(usuario == null) {
		        return ResponseEntity.notFound().build();
		    }
		return ResponseEntity.ok(usuarioMapper.toSalidaDTO(usuario));
	}
	
	@DeleteMapping("delete/{id}")
	public void deleteUsuario(@PathVariable Long id) {
		usuarioParticipanteService.deleteUsuarioParticipante(id);
	}
	
	@PutMapping("modificar/{id}")
	public ResponseEntity<UsuarioSalidaDTO> putUsuario(@RequestBody @Valid UsuarioEntradaDTO usuarioDTO, @PathVariable Long id) {	
		Usuario usuario = usuarioParticipanteService.modNombreUsuarioParticipante(id, usuarioDTO.getNombre());
		if(usuario == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(usuarioMapper.toSalidaDTO(usuario));	
	}
	
	@PostMapping("crear")
	public ResponseEntity<TokenResponse> postUsuario(@RequestBody @Valid UsuarioEntradaDTO usuarioDTO){
		Usuario usuarioEntrada = usuarioMapper.toUsuarioEntrada(usuarioDTO);
		TokenResponse token = usuarioParticipanteService.createUsuarioParticipante(usuarioEntrada);
		if(token == null) {
			return ResponseEntity.badRequest().build();
		}
	
		return ResponseEntity.ok(token);
		
	}
	
	//esto va de autenticar
//	@PostMapping("/login")
//	public ResponseEntity<TokenResponse> authenticate(@RequestBody final LoginRequest request) {
//		final TokenResponse token = service.register(request);
//		return ResponseEntity.ok(token);
//	}
//
//	//esto va de token
//	@PostMapping("/refresh")
//	public ResponseEntity<TokenResponse> refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader) {
//
//		return service.refreshToken(authHeader);
//	}
	
}
