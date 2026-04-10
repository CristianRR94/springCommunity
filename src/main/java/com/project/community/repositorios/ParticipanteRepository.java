package com.project.community.repositorios;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.community.entidades.Participante;


public interface ParticipanteRepository extends JpaRepository<Participante, Long>{
	Participante findByUsuarioId(Long usuarioId);
	
	//encontrar participante con amigos a partir de id usuario
	@EntityGraph(attributePaths = "amigos")
	Optional<Participante> findWithAmigosByUsuarioId(Long UsuarioId);
	
	//encontrar participante por id con lazy de tablas
	Optional<Participante> findById(Long idParticipante);
	
	//participante con amigos (no lazy de tabla amigos)
	@EntityGraph(attributePaths = "amigos")
	Optional<Participante> findWithAmigosById(Long id);
	
	//solo los amigos
	@Query("SELECT p.amigos FROM Participante p WHERE p.id= :id")
	Set<Participante> getAmigosPorId(Long id);
	
	//buscador participantes en amigos
	@Query("SELECT p FROM Participante p WHERE LOWER(p.nombreParticipante) LIKE LOWER(CONCAT('%', :input, '%'))"
			+ " AND p.usuario.id != :miId"
			+ " AND p NOT IN (SELECT a FROM Participante p2 JOIN p2.amigos a WHERE p2.usuario.id = :miId)")
	Set<Participante> buscarAmigos(@Param("input") String input, @Param("miId") Long miId);

}
