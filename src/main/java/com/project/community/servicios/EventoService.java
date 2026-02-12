package com.project.community.servicios;

import java.util.List;

import com.project.community.entidades.Evento;

public interface EventoService {
	public Evento getEvento(Long id);
	
	public List<Evento> getEventos();
	
	public Evento postEvento(Evento evento);
	
	public Evento putEvento(Evento evento);
	
	public void deleteEvento(Long id);
	
	public List<Evento> getEventosPorParticipanteId(Long idParticipante);
}
