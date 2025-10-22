package com.project.community.servicios;

import org.springframework.stereotype.Service;

import com.project.community.entidades.Usuario;
import com.project.community.repositorios.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService{
	
	
	private final UsuarioRepository usuarioRepository;
	
	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public Usuario getUsuario(Long id) {
		
		return usuarioRepository.findById(id).orElse(null);
	}

	@Override
	public Iterable<Usuario> getUsuarios() {
		
		return usuarioRepository.findAll();
	}

	@Override
	public Usuario postUsuario(Usuario usuario) {
		
		return usuarioRepository.save(usuario);
	}

	@Override
	public void deleteUsuario(Usuario usuario) {
		usuarioRepository.delete(usuario);
		
	}

	@Override
	public Usuario putUsuario(Usuario usuario) {
		
		return usuarioRepository.save(usuario);
	}

}
