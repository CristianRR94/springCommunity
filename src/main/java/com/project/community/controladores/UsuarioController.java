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
import com.project.community.servicios.AuthDataService;
import com.project.community.servicios.UsuarioParticipanteService;
import com.project.community.servicios.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
	
//zona privada
	private final UsuarioService usuarioService;
	private final UsuarioParticipanteService usuarioParticipanteService;
	private final UsuarioMapper usuarioMapper;
	private final AuthDataService authDataService;
	
	
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
	
	@DeleteMapping("delete")
	public void deleteUsuario() {
		Long id = authDataService.obtenerUsuarioAutenticado().getId();
		usuarioParticipanteService.deleteUsuarioParticipante(id);
	}
	
	@PutMapping("modificar")
	public ResponseEntity<UsuarioSalidaDTO> putUsuario(@RequestBody @Valid UsuarioEntradaDTO usuarioDTO) {	
		Long idUsuario = authDataService.obtenerUsuarioAutenticado().getId();
		Usuario usuario = usuarioParticipanteService.modNombreUsuarioParticipante(idUsuario, usuarioDTO.getNombre());
		if(usuario == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(usuarioMapper.toSalidaDTO(usuario));	
	}
	


	// esto va de token
	@PostMapping("refresh")
	public TokenResponse refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader) {

		return usuarioService.refresh(authHeader);
		
	}
	
}
