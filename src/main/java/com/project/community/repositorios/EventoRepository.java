package com.project.community.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;



import com.project.community.entidades.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long>{

}
