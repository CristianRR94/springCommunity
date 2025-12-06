package com.project.community.mapper;

import java.util.Collections;

import java.util.List;
import java.util.Set;

import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.project.community.DTO.ParticipanteAmigoDTO;
import com.project.community.DTO.ParticipanteDTO;
import com.project.community.entidades.Evento;
import com.project.community.entidades.Participante;

@Mapper(componentModel = "spring")
public interface ParticipanteMapper {
	//default para implementar un método en una interfaz
	default Long eventoId(Evento evento) {
		return evento.getId();
	}
	
	default List<Long> getEventoIds(Set<Evento> eventoIds){
		if(eventoIds == null) {
			return Collections.emptyList();
		}
		return eventoIds.stream().map(e->e.getId()).collect(Collectors.toList());
	}
	
	default Set<Long> getAmigoIds(Set<Participante> amigos){
		if(amigos == null) {
			return Collections.emptySet();
		}
		return amigos.stream().map(a->a.getId()).collect(Collectors.toSet());
	}
	
	
	@Mapping(target="eventosAdministradosId", source="eventosAdministrados")
	@Mapping(target="eventosId", source="eventos")
	@Mapping(target="usuarioId", ignore=true)
	ParticipanteDTO  toDTO(Participante participante);
	
	@Mapping(target="eventos", ignore=true)
	@Mapping(target="eventosAdministrados", ignore=true)
	@Mapping(target="usuario", ignore=true)
	@Mapping(target="amigos", ignore=true)
	Participante toParticipante(ParticipanteDTO participanteDTO);
	List<ParticipanteDTO> toDTOs(List<Participante> participantes);
	List<Participante> toParticipantes(List<ParticipanteDTO> participanteDTOs);
	
	@Mapping(target="usuarioId", source="usuario.id")
	@Mapping(target="amigosId", source="amigos")
	ParticipanteAmigoDTO toAmigoDTO(Participante participante);
	Set<ParticipanteAmigoDTO> toAmigoListDTO(Set<Participante> participantes);
	
}
