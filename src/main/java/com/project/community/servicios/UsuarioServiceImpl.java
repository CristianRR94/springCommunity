package com.project.community.servicios;

import java.util.List;

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
	public List<Usuario> getUsuarios() {
		
		return usuarioRepository.findAll();
	}

	@Override
	public Usuario postUsuario(Usuario usuario) {
		
		return usuarioRepository.save(usuario);
	}

	@Override
	public void deleteUsuario(Long id) {
		usuarioRepository.deleteById(id);
		
	}

	@Override
	public Usuario putUsuario(Usuario usuario) {
		
		return usuarioRepository.save(usuario);
	}

	@Override
	public Usuario encontrarNombre(String nombre) {
		
		return usuarioRepository.findByNombre(nombre);
	}

}
