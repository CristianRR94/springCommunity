package com.project.community.repositorios;

import org.springframework.data.jpa.repository.EntityGraph;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.community.entidades.Evento;
import java.util.List;
import java.util.Optional;
import java.util.Set;



public interface EventoRepository extends JpaRepository<Evento, Long>{
	@EntityGraph(attributePaths = {"participantesEvento", "administradores"})
	Optional<Evento> findById(Long id);
	
	Set<Evento> findByParticipantesEvento_Id(Long idParticipante);
	
	Boolean existsByIdAndParticipantesEventoId(Long eventoId, Long participanteId);
	
	Boolean existsByIdAndAdministradoresId(Long eventoId, Long participanteId);
	
	@Query("SELECT DISTINCT e FROM Evento e " +
	           "LEFT JOIN e.participantesEvento p " +
	           "WHERE (e.oculto = false AND e.privado = false) " +
	           "OR p.id = :participanteId")
	    List<Evento> findFeedEventos(@Param("participanteId") Long participanteId);
	
}
