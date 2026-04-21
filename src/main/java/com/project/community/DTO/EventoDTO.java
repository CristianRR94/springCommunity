package com.project.community.DTO;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoDTO {
	
	private Long id;	
	private String nombreEvento;
	private String tipoEvento;
	private LocalDate fechaEvento;
	private String informacion;
	private String chat;
	private String imagenEvento; 
	private boolean privado;
	private boolean oculto;
	private int maxNumParticipantes;
	private List<Long> participantesEvento;
	private List<Long> administradores;
}
