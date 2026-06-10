package com.project.community.DTO;


import java.util.Set;


public record ParticipanteAmigoDTO (
	 Long id,
	 String nombreParticipante,
	 Set<Long> amigosId,
	 Long usuarioId
	 )
{}
