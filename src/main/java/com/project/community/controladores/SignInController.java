package com.project.community.controladores;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.community.entidades.Participante;
import com.project.community.entidades.Usuario;

import com.project.community.servicios.UsuarioParticipanteServiceImpl;
import com.project.community.servicios.UsuarioServiceImpl;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/usuario")
public class SignInController {
	
	@Autowired
	private UsuarioServiceImpl usuarioService;
	@Autowired
	private UsuarioParticipanteServiceImpl usuarioParticipanteService;
	
	@GetMapping
	public Iterable<Usuario> getUsuarios() {
		return usuarioService.getUsuarios();
	}
	
	@GetMapping("{id}")
	public Usuario getUsuario(@PathVariable Long id) {
		return usuarioService.getUsuario(id);
	}
	
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
		usuarioService.deleteUsuario(usuario);
	}
}
