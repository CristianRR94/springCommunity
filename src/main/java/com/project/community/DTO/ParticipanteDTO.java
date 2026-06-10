package com.project.community.DTO;

import java.util.List;

public record ParticipanteDTO(
	 Long id,
	 String nombreParticipante,
	 List<Long> eventosId,
	 List<Long> eventosAdministradosId,
	 Long usuarioId,
	 String imagenParticipante
	) {
}
