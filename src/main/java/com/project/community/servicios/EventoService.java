package com.project.community.servicios;

import com.project.community.entidades.Evento;

public interface EventoService {
	public Evento getEvento(Long id);
	
	public Iterable<Evento> getEventos();
	
	public Evento postEvento(Evento evento);
	
	public Evento putEvento(Evento evento);
	
	public void deleteEvento(Long id);
}
