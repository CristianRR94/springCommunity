package com.project.community.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MensajeDTO(
		@NotBlank(message= "El mensaje no puede estar vacío")
		@Size(max=1000, message= "1000 caratceres máximo")
		String texto,
		String nombreParticipante,
		Long participanteId,
		Long eventoId
	) {
	
}
