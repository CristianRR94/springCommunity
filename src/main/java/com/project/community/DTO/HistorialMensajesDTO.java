package com.project.community.DTO;

import java.time.LocalDateTime;




public record HistorialMensajesDTO(
		Long id,
		String texto,
		Long participanteId,
		String nombreParticipante,
		LocalDateTime fechaEnvio
	) {

}
