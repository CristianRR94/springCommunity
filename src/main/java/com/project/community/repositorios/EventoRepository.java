package com.project.community.repositorios;

import org.springframework.data.jpa.repository.EntityGraph;


import org.springframework.data.jpa.repository.JpaRepository;
import com.project.community.entidades.Evento;
import java.util.List;
import java.util.Optional;



public interface EventoRepository extends JpaRepository<Evento, Long>{
	@EntityGraph(attributePaths = {"participantesEvento", "administradores"})
	Optional<Evento> findById(Long id);
	
	List<Evento> findByParticipantesEvento_Id(Long idParticipante);
	
	Boolean existsByIdAndParticipantesEventoId(Long eventoId, Long participanteId);
	

	Boolean existsByIdAndAdministradoresId(long eventoId, Long participanteId);
}
