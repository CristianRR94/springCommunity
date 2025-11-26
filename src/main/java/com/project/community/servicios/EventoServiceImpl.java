package com.project.community.servicios;

import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.community.entidades.Evento;
import com.project.community.repositorios.EventoRepository;

@Service
public class EventoServiceImpl implements EventoService{
	
	private final EventoRepository eventoRepository;
	
	public EventoServiceImpl(EventoRepository eventoRepository) {
		this.eventoRepository = eventoRepository;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Evento getEvento(Long id) {

		return eventoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Evento no encontrado con id: " + id));
	}

	@Override
	@Transactional
	public List<Evento> getEventos() {

		return eventoRepository.findAll();
	}

	@Override
	@Transactional
	public Evento postEvento(Evento evento) {

		return eventoRepository.save(evento);
	}

	@Override
	@Transactional
	public Evento putEvento(Evento evento) {

		return eventoRepository.save(evento);
	}

	@Override
	@Transactional
	public void deleteEvento(Long id) {
		Evento evento = eventoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Evento no encontrado con id: " + id));
		eventoRepository.delete(evento);
	}

}
