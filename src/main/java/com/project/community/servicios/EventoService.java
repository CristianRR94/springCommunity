package com.project.community.servicios;

import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.project.community.DTO.EventoDTO;
import com.project.community.DTO.EventoPrincipalDTO;
import com.project.community.entidades.Evento;

public interface EventoService {
	public Evento getEvento(Long id);
	
	public Set<Evento> getEventos();
	
	public Evento postEvento(EventoDTO evento);
	
	public EventoDTO putEvento(EventoDTO evento, MultipartFile imagen, Long eventoId);
	
	public void deleteEvento(Long id);
	
	public Set<Evento> getEventosPorParticipanteId(Long idParticipante);
	
	public EventoDTO getEventoDTO(Long id);
	
	public List<EventoPrincipalDTO> getEventosPorParticipantePrincipalDTO();
	
	public EventoDTO createEventoDTO(EventoDTO dto, MultipartFile imagen);
	
	public List<EventoPrincipalDTO> getEventosPrincipalDTO();
	
	public void deleteEventoDTO();
}
