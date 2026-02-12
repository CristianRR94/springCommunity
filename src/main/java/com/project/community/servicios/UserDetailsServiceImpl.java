package com.project.community.servicios;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.community.entidades.Usuario;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	private final UsuarioService usuarioService;
	
	public UserDetailsServiceImpl(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;

	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario  = usuarioService.encontrarNombre(username);
		if(usuario == null) {
			 throw new UsernameNotFoundException("Usuario no encontrado");
		}
		return usuario;
//		return User.builder()
//			    .username(usuario.getNombre())  //cuidado aquí
//			    .password(usuario.getPassword())
//			    .roles(usuario.getRol())
//			    .build();	
	}
	
	
}
