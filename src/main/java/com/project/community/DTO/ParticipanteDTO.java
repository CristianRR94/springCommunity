package com.project.community.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipanteDTO {
	private Long id;
	private String nombreParticipante;
	private List<Long> eventosId;
	private List<Long> eventosAdministradosId;
	private Long usuarioId;
}
