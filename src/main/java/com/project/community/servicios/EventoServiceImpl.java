package com.project.community.servicios;

import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.community.entidades.Evento;
import com.project.community.entidades.Participante;
import com.project.community.repositorios.EventoRepository;

@Service
public class EventoServiceImpl implements EventoService{
	
	private final EventoRepository eventoRepository;
	private final AuthDataService authDataService;
	
	public EventoServiceImpl(EventoRepository eventoRepository, AuthDataService authDataService) {
		this.eventoRepository = eventoRepository;
		this.authDataService = authDataService;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Evento getEvento(Long id) {

		Evento evento = eventoRepository.findById(id).orElseThrow(
				() -> new IllegalArgumentException("Evento no encontrado con id: " + id)
				);

		return evento;
	}

	@Override
	@Transactional
	public List<Evento> getEventos() {

		return eventoRepository.findAll();
	}

	@Override
	@Transactional
	public Evento postEvento(Evento evento) {
		Participante participante = authDataService.obtenerParticipanteAutenticado();
		evento.addParticipante(participante);
		evento.addAdministrador(participante);
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

	@Override
	public List<Evento> getEventosPorParticipanteId(Long idParticipante) {
		
		return eventoRepository.findByParticipantesEvento_Id(idParticipante);
	}
	

}
