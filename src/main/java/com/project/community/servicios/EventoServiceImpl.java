package com.project.community.servicios;

import java.util.List;



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.community.DTO.EventoDTO;
import com.project.community.dominio.EventoNotFoundException;
import com.project.community.entidades.Evento;
import com.project.community.entidades.Participante;
import com.project.community.mapper.EventoMapper;
import com.project.community.repositorios.EventoRepository;
import com.project.community.repositorios.ParticipanteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventoServiceImpl implements EventoService{

    private final ParticipanteRepository participanteRepository;
	private final EventoRepository eventoRepository;
	private final AuthDataService authDataService;
	private final EventoMapper mapper;



	
	@Override
	@Transactional(readOnly = true)
	public Evento getEvento(Long id) {

		Evento evento = eventoRepository.findById(id).orElseThrow(
				() -> new IllegalArgumentException("Evento no encontrado con id: " + id)
				);

		return evento;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Evento> getEventos() {

		return eventoRepository.findAll();
	}

	@Override
	@Transactional
	public Evento postEvento(EventoDTO dto) {
		Evento evento = mapper.toEvento(dto);
		Participante participante = authDataService.obtenerParticipanteAutenticado();
		evento.addCreadorComoAdmin(participante);
		//cambio para invitacion
		if(dto.getParticipantesEvento() != null) {
			dto.getParticipantesEvento().forEach(id -> {
				participanteRepository.findById(id).ifPresent(
						amigo->{
							evento.addParticipante(
									amigo);
						});
			});
		}
		return eventoRepository.save(evento);
	}

	@Override
	@Transactional
	public Evento putEvento(EventoDTO dto) {
		Evento evento = mapper.toEvento(dto);
		return eventoRepository.save(evento);
	}

	@Override
	@Transactional
	public void deleteEvento(Long id) {
		Evento evento = eventoRepository.findById(id).orElseThrow(() -> new EventoNotFoundException("Evento no encontrado con id: " + id));
		eventoRepository.delete(evento);
	}

	@Override
	public List<Evento> getEventosPorParticipanteId(Long idParticipante) {
		
		return eventoRepository.findByParticipantesEvento_Id(idParticipante);
	}
	

}
