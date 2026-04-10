package com.project.community.mapper;

import java.util.Collections;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.project.community.DTO.EventoDTO;
import com.project.community.DTO.EventoPrincipalDTO;
import com.project.community.entidades.Evento;
import com.project.community.entidades.Participante;

@Mapper(componentModel =  "spring")
public interface EventoMapper {
	
	default Long participanteId(Participante participante){
		return participante.getId();
	}
	
	default List<Long> getParticipantesId(Set<Participante> participantes){
		if(participantes == null) {
			return Collections.emptyList();
		}
		Stream<Long> participantesId = participantes.stream().map(p->participanteId(p));
		return participantesId.collect(Collectors.toList());
	}
	
	
	@Mapping(target = "participanteIds", source = "participantesEvento")
	@Mapping(target = "administradorIds", source = "administradores")
	EventoDTO toDTO(Evento evento);
	@Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "participantesEvento", ignore=true)
	@Mapping(target = "administradores", ignore=true)
	Evento toEvento(EventoDTO eventoDTO);
	List<EventoDTO> toDTOs(List<Evento> eventos);
	List<Evento> toEventos(List<EventoDTO> eventoDTOs);
	
	EventoPrincipalDTO toPrincipalDTO(Evento evento);
	@Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "participantesEvento", ignore=true)
	@Mapping(target = "administradores", ignore=true)
	@Mapping(target = "chat", ignore=true)
	@Mapping(target = "maxNumParticipantes", ignore=true)
	@Mapping(target = "oculto", ignore=true)
	@Mapping(target = "privado", ignore=true)
	@Mapping(target = "informacion", ignore=true)
	Evento toEventoPrincipal(EventoPrincipalDTO eventoDTO);
	List<Evento> toEventoPrincipalList(List<EventoPrincipalDTO> eventoDTO);
	List<EventoPrincipalDTO> toPrincipalDTOList(List<Evento> eventos);
	
}
