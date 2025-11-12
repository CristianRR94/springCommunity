package com.project.community.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.project.community.DTO.EventoDTO;
import com.project.community.entidades.Evento;

@Mapper(componentModel =  "spring")
public interface EventoMapper {
	EventoDTO toDTO(Evento evento);
	@Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "administradores", ignore = true)
    @Mapping(target = "participantesEvento", ignore = true)
	Evento toEvento(EventoDTO eventoDTO);
	Iterable<EventoDTO> toDTOs(Iterable<Evento> eventos);
	Iterable<Evento> toEventos(Iterable<EventoDTO> eventoDTOs);
}
