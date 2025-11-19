package com.project.community.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.community.DTO.UsuarioEntradaDTO;
import com.project.community.DTO.UsuarioSalidaDTO;
import com.project.community.entidades.Participante;
import com.project.community.entidades.Usuario;
import com.project.community.mapper.UsuarioMapper;
import com.project.community.servicios.UsuarioParticipanteServiceImpl;
import com.project.community.servicios.UsuarioServiceImpl;

import jakarta.validation.Valid;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/usuario")
public class SignInController {
	
	@Autowired
	private final UsuarioServiceImpl usuarioService;
	private final UsuarioParticipanteServiceImpl usuarioParticipanteService;
	private final UsuarioMapper usuarioMapper;
	
	
	public SignInController(UsuarioMapper usuarioMapper, 
			UsuarioParticipanteServiceImpl usuarioParticipanteService, 
			UsuarioServiceImpl usuarioService) {
		this.usuarioMapper = usuarioMapper;
		this.usuarioParticipanteService = usuarioParticipanteService;
		this.usuarioService = usuarioService;
	}
	
	@GetMapping
	public List<UsuarioSalidaDTO> getUsuarios(){
	List<Usuario> usuarios = usuarioService.getUsuarios();
	return usuarioMapper.toSalidaDTOs(usuarios);
	}
	
	@GetMapping("{id}")
	public UsuarioSalidaDTO getUsuario(@PathVariable Long id) {
		Usuario usuario = usuarioService.getUsuario(id);
		return usuarioMapper.toSalidaDTO(usuario);
	}
	
	@DeleteMapping("delete/{id}")
	public void deleteUsuario(@PathVariable Long id) {
		usuarioParticipanteService.deleteUsuarioParticipante(id);
	}
	
	@PutMapping("modificar/{id}")
	public Usuario putUsuario(@RequestBody UsuarioEntradaDTO usuario, @PathVariable Long id, String nombre) {
		
		usuarioParticipanteService.modNombreUsuarioParticipante(id, nombre);
		return usuarioMapper.toUsuarioEntrada(usuario);
		
	}
//	@GetMapping
//	public Iterable<Usuario> getUsuarios() {
//		return usuarioService.getUsuarios();
//	}
//	
//	@GetMapping("{id}")
//	public Usuario getUsuario(@PathVariable Long id) {
//		return usuarioService.getUsuario(id);
//	}
	
	/*@PostMapping
	public Usuario postUsuario(@RequestBody Usuario usuario) {
		return usuarioService.postUsuario(usuario);
	}*/
	
	@PostMapping
	public Usuario postUsuario(@RequestBody Usuario usuario) {
		return usuarioParticipanteService.createUsuarioParticipante(usuario, new Participante());
	}
	
	@PutMapping("put/{id}")
	public Usuario putUsuario(@RequestBody Usuario usuario) {

		return usuarioService.putUsuario(usuario);
	}
	
	@DeleteMapping("delete/{id}")
	public void deleteUsuario(Usuario usuario) {
		usuarioService.deleteUsuario(usuario.getId());
	}
}
