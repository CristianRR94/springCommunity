package com.project.community.servicios;

import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.community.DTO.EventoDTO;
import com.project.community.dominio.EventoNotFoundException;
import com.project.community.dominio.ParticipanteException;
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
	private final ImageService imageService;
	
	private static final String IMAGE_PLACEHOLDER = "eventos/default.png";

	
	@Override
	@Transactional(readOnly = true)
	public Evento getEvento(Long id) {

		Evento evento = eventoRepository.findById(id).orElseThrow(
				() -> new EventoNotFoundException("Evento no encontrado con id: " + id)
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
		evento.validarFecha();
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
		if(evento.getImagenEvento() == null || evento.getImagenEvento().isEmpty()) {
			evento.setImagenEvento(IMAGE_PLACEHOLDER);
		}
		return eventoRepository.save(evento);
	}

	@Override
	@Transactional
	public Evento putEvento(EventoDTO dto, Long idParticipante) {
		Evento eventoActual = eventoRepository.findById(dto.getId())
				.orElseThrow(()-> new EventoNotFoundException("Error al detectar el evento actual"));
		if(idParticipante == null || !participanteRepository.existsById(idParticipante)) {
			throw new ParticipanteException("Participante no encontrado");
		}
		
		boolean esAdmin = eventoActual.getAdministradores().stream()
				.anyMatch(admin -> admin.getId().equals(idParticipante));
		if(!esAdmin) {
			throw new ParticipanteException("El participante no tiene permiso para modificar el evento");
		}
		String viejaImagen = eventoActual.getImagenEvento();
		String nuevaImagen = dto.getImagenEvento();
		eventoActual.actualizarEvento(
		        dto.getNombreEvento(),
		        dto.getTipoEvento(),
		        dto.getFechaEvento(),
		        dto.getInformacion(),
		       // dto.getChat(),
		        dto.isPrivado(),
		        dto.isOculto(),
		        dto.getMaxNumParticipantes()
		    );
		
		if(nuevaImagen != null && !nuevaImagen.equals(viejaImagen)) {
			eventoActual.setImagenEvento(nuevaImagen);
			if(viejaImagen != null && !viejaImagen.isEmpty() && !viejaImagen.equals(IMAGE_PLACEHOLDER)) {
				imageService.deleteImage(viejaImagen);
			}
		}
		
		return eventoRepository.save(eventoActual);
	}

	@Override
	@Transactional
	public void deleteEvento(Long eventoId, Long participanteId) {
		Evento evento = eventoRepository.findById(eventoId).orElseThrow(() -> new EventoNotFoundException("Evento no encontrado"));
		boolean esAdmin = evento.getAdministradores().stream()
				.anyMatch(admin -> admin.getId().equals(participanteId));
		if(!esAdmin) {
			throw new ParticipanteException("El participante no tiene permiso para eliminar el evento");
		}
		eventoRepository.delete(evento);
		//como no eliminamos de la db si no de memoria, va después
		if(evento.getImagenEvento() != null && !evento.getImagenEvento().isEmpty() && !evento.getImagenEvento().equals(IMAGE_PLACEHOLDER)) {
			imageService.deleteImage(evento.getImagenEvento());
		}
		
	}

	@Override
	public List<Evento> getEventosPorParticipanteId(Long idParticipante) {
		
		return eventoRepository.findByParticipantesEvento_Id(idParticipante);
	}
	
	
}
