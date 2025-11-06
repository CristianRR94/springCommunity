package com.project.community.servicios;

import org.springframework.stereotype.Service;

import com.project.community.entidades.Evento;
import com.project.community.repositorios.EventoRepository;

@Service
public class EventoServiceImpl implements EventoService{
	
	private final EventoRepository eventoRepository;
	
	public EventoServiceImpl(EventoRepository eventoRepository) {
		this.eventoRepository = eventoRepository;
	}
	
	@Override
	public Evento getEvento(Long id) {

		return eventoRepository.findById(id).orElse(null);
	}

	@Override
	public Iterable<Evento> getEventos() {

		return eventoRepository.findAll();
	}

	@Override
	public Evento postEvento(Evento evento) {

		return eventoRepository.save(evento);
	}

	@Override
	public Evento putEvento(Evento evento) {

		return eventoRepository.save(evento);
	}

	@Override
	public void deleteEvento(Evento evento) {

		eventoRepository.delete(evento);
	}

}
