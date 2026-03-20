package com.project.community.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.community.entidades.Participante;


public interface ParticipanteRepository extends JpaRepository<Participante, Long>{
	Participante findByUsuarioId(Long usuarioId);
	
	Optional<Participante> findById(Long idParticipante);
	
	@EntityGraph(attributePaths = "amigos")
	Optional<Participante> findWithAmigosById(Long id);
	
	
	

}
