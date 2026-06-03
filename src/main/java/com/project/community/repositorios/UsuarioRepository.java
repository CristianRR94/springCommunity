package com.project.community.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;



import com.project.community.entidades.Usuario;





public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	Optional<Usuario> findByNombre(String nombre);
	Optional<Usuario> findByEmail(String email);
	boolean existsByNombre(String nombre);
}
