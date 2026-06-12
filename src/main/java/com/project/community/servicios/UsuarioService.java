package com.project.community.servicios;

import java.util.List;


import com.project.community.entidades.Usuario;



public interface UsuarioService{

	public Usuario getUsuario(Long id);
	
	public List<Usuario> getUsuarios();
	
	public Usuario encontrarNombre(String nombre);
	
	public Usuario postUsuario(Usuario usuario);
	
	public void deleteUsuario(Long id);
	
	public Usuario putUsuario(Usuario usuario);
	
	public boolean existeNombre(String nombre);

	Long getId(String token);
}
