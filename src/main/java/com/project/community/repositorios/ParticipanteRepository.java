package com.project.community.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.community.entidades.Participante;


public interface ParticipanteRepository extends JpaRepository<Participante, Long>{
	Participante findByUsuarioId(Long usuarioId);
}
