package com.project.community.DTO;

import java.time.LocalDate;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//como hay setters en el controlador, lo dejamos como dto
public class EventoDTO {
	
	private Long id;	
	
	@NotBlank(message = "El nombre del evento es obligatorio")
	@Size(max = 255, message = "El nombre del evento no puede superar los 255 caracteres")
	private String nombreEvento;
	
	@Size(max = 255, message = "El tipo de evento no puede superar los 255 caracteres") 
	private String tipoEvento;
	
	private LocalDate fechaEvento;
	private String informacion;
	private String chat;
	private String imagenEvento; 
	private boolean privado;
	private boolean oculto;
	
	@Min(value = 0, message = "El número mínimo de participantes es 0")
	@Max(value = 255, message = "El número máximo de participantes no puede superar los 255")
	@Builder.Default
	private int maxNumParticipantes = 255;
	
	private List<Long> participantesEvento;
	private List<Long> administradores;
	
	
}