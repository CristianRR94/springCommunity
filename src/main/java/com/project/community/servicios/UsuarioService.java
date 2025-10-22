package com.project.community.servicios;

import com.project.community.entidades.Usuario;



public interface UsuarioService{

	public Usuario getUsuario(Long id);
	
	public Iterable<Usuario> getUsuarios();
	
	public Usuario postUsuario(Usuario usuario);
	
	public void deleteUsuario(Usuario usuario);
	
	public Usuario putUsuario(Usuario usuario);
}
