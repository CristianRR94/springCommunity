package com.project.community.DTO;


import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipanteAmigoDTO {
	private Long id;
	private String nombreParticipante;
	private Set<Long> amigosId;
	private Long usuarioId;
}
