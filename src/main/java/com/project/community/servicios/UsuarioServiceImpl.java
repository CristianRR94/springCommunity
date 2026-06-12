package com.project.community.servicios;

import java.util.List;


import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.community.entidades.Usuario;
import com.project.community.repositorios.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService{

    private final PasswordEncoder passwordEncoder;
	private final UsuarioRepository usuarioRepository;
	private final JwtProviderService jwtProviderService;


	
	

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
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
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
		
		return usuarioRepository.findByNombre(nombre).orElseThrow(()-> new UsernameNotFoundException(nombre + "no encontrado."));
	}	

	@Override
	public boolean existeNombre(String nombre) {
		return usuarioRepository.existsByNombre(nombre);
	}

	@Override
	public Long getId(String token) {
		return jwtProviderService.extractId(token);
	}

}
