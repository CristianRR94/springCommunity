package com.project.community.servicios;

import java.util.List;

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

		return eventoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Evento no encontrado con id: " + id));
	}

	@Override
	public List<Evento> getEventos() {

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
	public void deleteEvento(Long id) {
		Evento evento = eventoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Evento no encontrado con id: " + id));
		eventoRepository.delete(evento);
	}

}
