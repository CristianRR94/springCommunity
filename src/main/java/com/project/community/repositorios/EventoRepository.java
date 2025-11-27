package com.project.community.repositorios;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.community.entidades.Evento;
import java.util.Optional;


public interface EventoRepository extends JpaRepository<Evento, Long>{
	@EntityGraph(attributePaths = {"participantesEvento", "administradores"})
	Optional<Evento> findById(Long id);
}
