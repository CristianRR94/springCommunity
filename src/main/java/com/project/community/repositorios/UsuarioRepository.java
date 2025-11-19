package com.project.community.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;


import com.project.community.entidades.Usuario;



public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	Usuario findByNombre(String nombre);
	
	 
}
