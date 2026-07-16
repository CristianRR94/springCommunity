package com.project.community.servicios;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.community.DTO.HistorialMensajesDTO;
import com.project.community.DTO.MensajeDTO;
import com.project.community.entidades.Evento;
import com.project.community.entidades.Mensaje;
import com.project.community.entidades.Participante;
import com.project.community.repositorios.EventoRepository;
import com.project.community.repositorios.MensajeRepository;
import com.project.community.repositorios.ParticipanteRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MensajeServiceImpl implements MensajeService{
	
	private final MensajeRepository mensajeRepository;
	private final EventoRepository eventoRepository;
	private final ParticipanteRepository participanteRepository;

	@Override
	@Transactional
	public HistorialMensajesDTO guardarMensaje(Long eventoId, MensajeDTO mensajeDto, String nombre) {
		//getReference para meter como foreign key
		Evento evento = eventoRepository.getReferenceById(eventoId);
		Participante emisor = participanteRepository.findByUsuarioNombre(nombre)
				.orElseThrow(() -> new IllegalArgumentException("El usuario no es un participante válido"));
		boolean esParticipanteReal = eventoRepository.existsByIdAndParticipantesEventoId(eventoId, emisor.getId());
		if (!esParticipanteReal) {
	        throw new SecurityException("No puedes escribir en un evento donde no estás inscrito.");
	    }
		Mensaje mensaje = new Mensaje();
		mensaje.setTexto(mensajeDto.texto());
		mensaje.setEvento(evento);
		mensaje.setEmisor(emisor);
		Mensaje mensajeGuardado = mensajeRepository.save(mensaje);
		return new HistorialMensajesDTO(
                mensajeGuardado.getId(),
                mensajeGuardado.getTexto(),
                emisor.getId(),
                emisor.getNombreParticipante(),
                mensajeGuardado.getCreatedAt()
                );
	}

	@Override
	@Transactional(readOnly = true)
	public List<HistorialMensajesDTO> obtenerHistorial(Long eventoId) {
		return mensajeRepository.findByEventoIdOrderByCreatedAtAsc(eventoId)
			.stream()
			.map((mensaje)->new HistorialMensajesDTO(
					mensaje.getId(),
					mensaje.getTexto(),
					mensaje.getEmisor().getId(),
					mensaje.getEmisor().getNombreParticipante(),
					mensaje.getCreatedAt()))
			.collect(Collectors.toList());
	}

}
