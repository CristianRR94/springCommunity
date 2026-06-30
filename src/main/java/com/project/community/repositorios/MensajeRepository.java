package com.project.community.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.community.entidades.Mensaje;

public interface MensajeRepository extends JpaRepository<Mensaje, Long>{
	List<Mensaje> findByEventoIdOrderByCreatedAtAsc(Long eventoId);
}
